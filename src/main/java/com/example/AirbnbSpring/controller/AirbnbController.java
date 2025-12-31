package com.example.AirbnbSpring.controller;

import com.example.AirbnbSpring.model.Airbnb;
import com.example.AirbnbSpring.services.IAirbnbService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/airbnbs")
@RequiredArgsConstructor
public class AirbnbController {

    private final IAirbnbService airbnbService;
    @PostMapping
    public ResponseEntity<Airbnb> createAirbnb(@RequestBody @Valid Airbnb airbnb) {
        return new ResponseEntity<>(
                airbnbService.createAirbnb(airbnb),
                HttpStatus.CREATED
        );
    }

    // GET AIRBNB BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Airbnb> getAirbnbById(@PathVariable Long id) {
        return ResponseEntity.ok(
                airbnbService.getAirbnbById(id)
        );
    }
}
