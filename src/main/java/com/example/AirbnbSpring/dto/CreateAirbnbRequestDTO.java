package com.example.AirbnbSpring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class CreateAirbnbRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Price per night is required")
    private Double pricePerNight;

    @NotBlank(message = "Location is required")
    private String location;
}
