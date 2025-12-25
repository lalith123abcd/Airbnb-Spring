package com.example.AirbnbSpring.repository.writes;

import com.example.AirbnbSpring.model.Airbnb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirbnbWriteRepository extends JpaRepository<Airbnb,Long> {
}
