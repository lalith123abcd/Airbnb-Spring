package com.example.AirbnbSpring.services;

import com.example.AirbnbSpring.model.Airbnb;

import java.util.List;

public interface IAirbnbService {
    Airbnb createAirbnb(Airbnb airbnb);

    Airbnb getAirbnbById(Long id);


}
