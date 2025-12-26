package com.example.AirbnbSpring.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateBookingRequest {

    @NotNull(message = "airbnb id is required")
    private Long airbnbId;

    @NotNull(message = "checkindate id is required")
    private LocalDate checkInDate;
    @NotNull(message = "checkOutDate id is required")
    private LocalDate checkOutDate;
    @NotNull(message = "user id is required")
    private Long userId;


}
