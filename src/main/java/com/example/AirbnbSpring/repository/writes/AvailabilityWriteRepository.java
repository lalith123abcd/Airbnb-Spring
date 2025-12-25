package com.example.AirbnbSpring.repository.writes;

import com.example.AirbnbSpring.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityWriteRepository extends JpaRepository<Availability,Long> {

    List<Availability> findByBookingId(Long bookingId);

    List<Availability> findByAirbnbId(Long airbnbId);
}
