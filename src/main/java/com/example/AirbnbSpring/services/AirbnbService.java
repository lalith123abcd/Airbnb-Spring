package com.example.AirbnbSpring.services;

import com.example.AirbnbSpring.model.Airbnb;
import com.example.AirbnbSpring.repository.writes.AirbnbWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirbnbService  implements IAirbnbService{

    private final AirbnbWriteRepository airbnbWriteRepository;

    @Override
    public Airbnb createAirbnb(Airbnb airbnb) {
        return airbnbWriteRepository.save(airbnb);
    }

    @Override
    public Airbnb getAirbnbById(Long id) {
        return airbnbWriteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Airbnb not found"));
    }


}
