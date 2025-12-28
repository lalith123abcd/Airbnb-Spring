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
import com.example.AirbnbSpring.services.concurrency.ConcurrencyControlStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

        redisWriteRepository.writeBookingReadModel(booking);

        return booking;


    }

    @Override
    public Booking updateBooking(UpdateBookingRequest updateBookingRequest) {
        return null;
    }
}
