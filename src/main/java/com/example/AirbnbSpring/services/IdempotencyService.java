package com.example.AirbnbSpring.services;

import com.example.AirbnbSpring.model.Booking;
import com.example.AirbnbSpring.model.readModels.BookingReadModel;
import com.example.AirbnbSpring.repository.reads.RedisReadRepository;
import com.example.AirbnbSpring.repository.writes.BookingWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdempotencyService implements IIdempotencyServices{

    private final RedisReadRepository redisReadRepository;

    private final BookingWriteRepository bookingWriteRepository;
    @Override
    public boolean isIdempotencyKeyUsed(String idempotencyKey) {
        return false;
    }

    @Override
    public Optional<Booking> findBookingByIdempotencyKey(String idempotencyKey) {
        BookingReadModel bookingReadModel=redisReadRepository.findBookingByIdempotencyKey(idempotencyKey);

        if(bookingReadModel!=null){

        }

        bookingWriteRepository.findByIdempotencyKey(idempotencyKey);
    }
}
