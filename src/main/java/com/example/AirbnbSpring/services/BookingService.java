package com.example.AirbnbSpring.services;

import com.example.AirbnbSpring.dto.CreateBookingRequest;
import com.example.AirbnbSpring.dto.UpdateBookingRequest;
import com.example.AirbnbSpring.model.Airbnb;
import com.example.AirbnbSpring.model.Availability;
import com.example.AirbnbSpring.model.Booking;
import com.example.AirbnbSpring.model.User;
import com.example.AirbnbSpring.repository.reads.RedisWriteRepository;
import com.example.AirbnbSpring.repository.writes.AirbnbWriteRepository;
import com.example.AirbnbSpring.repository.writes.AvailabilityWriteRepository;
import com.example.AirbnbSpring.repository.writes.BookingWriteRepository;
import com.example.AirbnbSpring.repository.writes.UserWriteRepository;
import com.example.AirbnbSpring.saga.SagaEventPublisher;
import com.example.AirbnbSpring.services.concurrency.ConcurrencyControlStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService implements IBookingService {

    private final BookingWriteRepository bookingWriteRepository;
    private final AvailabilityWriteRepository availabilityWriteRepository;

    private final AirbnbWriteRepository airbnbWriteRepository;
    private final ConcurrencyControlStrategy concurrencyControlStrategy;

    private final UserWriteRepository userWriteRepository;
    private final RedisWriteRepository redisWriteRepository;

    private final IIdempotencyServices iIdempotencyServices;
    private final SagaEventPublisher sagaEventPublisher;


    @Override
    @Transactional
    public Booking createBooking(CreateBookingRequest createBookingRequest) {
        Airbnb airbnb=airbnbWriteRepository.findById(createBookingRequest.getAirbnbId())
                .orElseThrow(()-> new RuntimeException("airbnb not found with this id"+createBookingRequest.getAirbnbId()));


        if(createBookingRequest.getCheckInDate().isAfter(createBookingRequest.getCheckOutDate())){
            throw new RuntimeException("check in date must be before checkout date");
        }

        if(createBookingRequest.getCheckInDate().isBefore(LocalDate.now())){
            throw new RuntimeException("check in date must be from today only");
        }

        List<Availability> availabilities=concurrencyControlStrategy.lockAndCheckAvailability(createBookingRequest.getAirbnbId(),createBookingRequest.getCheckInDate()
        ,createBookingRequest.getCheckOutDate(), createBookingRequest.getUserId());

        Long nights= ChronoUnit.DAYS.between(createBookingRequest.getCheckInDate(),createBookingRequest.getCheckOutDate());

        double pricePerNight=airbnb.getPricePerNight();

        double totalPrice=pricePerNight*nights;
        User user=userWriteRepository.findById(createBookingRequest.getUserId()).orElseThrow(
                ()-> new RuntimeException("user not found")
        );
        String idempotencyKey=UUID.randomUUID().toString();

        log.info("creating booking with airbnb{} with checkInDate{} and checkOutDate{}" +
                "and idempotencyKey{}",airbnb,createBookingRequest.getCheckInDate()
        ,createBookingRequest.getCheckOutDate(),idempotencyKey);


        Booking booking=Booking.builder()
                .airbnb(airbnb)
                .user(user)
                .totalPrice(totalPrice)
                .idempotencyKey(idempotencyKey)
                .bookingStatus(Booking.BookingStatus.PENDING)
                .checkInDate(createBookingRequest.getCheckInDate())
                .checkOutDate(createBookingRequest.getCheckOutDate())
                .build();

        booking=bookingWriteRepository.save(booking);
        log.info("✅ Booking status updated to CONFIRMED in MySQL for bookingId={}",
                booking.getId());


        redisWriteRepository.writeBookingReadModel(booking);

        return booking;


    }

    @Override
    @Transactional
    public Booking updateBooking(UpdateBookingRequest updateBookingRequest) {
        log.info("Updating booking for idempotency key {}", updateBookingRequest.getIdempotencyKey());
        Booking booking = iIdempotencyServices.findBookingByIdempotencyKey(updateBookingRequest.getIdempotencyKey())
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        log.info("Booking found for idempotency key {}", updateBookingRequest.getIdempotencyKey());
        log.info("Booking status: {}", booking.getBookingStatus());
        if(booking.getBookingStatus() != Booking.BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not pending");

        }
        log.info("📤 Publishing saga event: BOOKING_CONFIRM_REQUESTED for bookingId={}",
                booking.getId());


        if(updateBookingRequest.getBookingStatus() == Booking.BookingStatus.CONFIRMED) { // TODO: This also violates a SOLID principle, please resolve: https://github.com/singhsanket143/AirbnbSpring/issues/13
            sagaEventPublisher.publishEvent(
                    "BOOKING_CONFIRM_REQUESTED",
                    "CONFIRM_BOOKING",
                    Map.of(
                            "bookingId", booking.getId(),
                            "airbnbId", booking.getAirbnb().getId(), // ✅ ONLY ID
                            "checkInDate", booking.getCheckInDate().toString(),
                            "checkOutDate", booking.getCheckOutDate().toString()
                    )
            );

        } else if(updateBookingRequest.getBookingStatus() == Booking.BookingStatus.CANCELLED) {
            sagaEventPublisher.publishEvent("BOOKING_CANCEL_REQUESTED", "CANCEL_BOOKING", Map.of("bookingId", booking.getId(), "airbnbId", booking.getAirbnb().getId(), "checkInDate", booking.getCheckInDate(), "checkOutDate", booking.getCheckOutDate()));
        }

        return booking;

    }
}
