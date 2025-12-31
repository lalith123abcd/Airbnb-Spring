package com.example.AirbnbSpring.mapper;

import com.example.AirbnbSpring.model.Booking;
import com.example.AirbnbSpring.model.readModels.BookingReadModel;
import com.example.AirbnbSpring.repository.writes.AirbnbWriteRepository;
import com.example.AirbnbSpring.repository.writes.UserWriteRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@RequiredArgsConstructor
@Component
@Builder
public class BookingMapper {
    private   final AirbnbWriteRepository airbnbWriteRepository;
    private   final UserWriteRepository userWriteRepository;


    public  Booking toEntity(BookingReadModel bookingReadModel){


            Booking booking = Booking.builder()
                    .id(bookingReadModel.getId())
                    .airbnb(
                            airbnbWriteRepository.findById(bookingReadModel.getAirbnbId())
                                    .orElseThrow(() -> new RuntimeException("Airbnb not found"))
                    )
                    .user(userWriteRepository.findById(bookingReadModel.getUserId())
                            .orElseThrow(()->new RuntimeException("user id not found")))
                    .totalPrice(bookingReadModel.getTotalPrice())
                    .bookingStatus(Booking.BookingStatus.valueOf(bookingReadModel.getBookingStatus()))
                    .idempotencyKey(bookingReadModel.getIdempotencyKey())
                    .checkInDate(bookingReadModel.getCheckInDate())
                    .checkOutDate(bookingReadModel.getCheckOutDate())
                    .build();

            return  booking;
        }

    }


