package com.example.AirbnbSpring.services;

import com.example.AirbnbSpring.dto.CreateBookingRequest;
import com.example.AirbnbSpring.dto.UpdateBookingRequest;
import com.example.AirbnbSpring.model.Booking;

public interface IBookingService {

    Booking createBooking(CreateBookingRequest createBookingRequest);

    Booking updateBooking(UpdateBookingRequest updateBookingRequest);
}
