package com.example.AirbnbSpring.services;

import com.example.AirbnbSpring.model.Booking;

import java.util.Optional;

public interface IIdempotencyServices {
    boolean isIdempotencyKeyUsed(String idempotencyKey);

    Optional<Booking> findBookingByIdempotencyKey(String idempotencyKey);
}
