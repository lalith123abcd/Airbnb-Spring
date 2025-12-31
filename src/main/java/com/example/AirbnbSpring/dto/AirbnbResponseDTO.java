package com.example.AirbnbSpring.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AirbnbResponseDTO {
    private Long id;
    private String name;
    private String description;
    private double pricePerNight;
    private String location;
}
