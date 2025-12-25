package com.example.AirbnbSpring.model.readModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirbnbReadModel {
    private Long id;

    private String name;

    private String description;

    private String location;

    private Long pricePerNight;

    private List<AvailabilityReadModel> availability;
}
