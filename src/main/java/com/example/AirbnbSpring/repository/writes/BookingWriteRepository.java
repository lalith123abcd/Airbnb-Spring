package com.example.AirbnbSpring.repository.writes;

import com.example.AirbnbSpring.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingWriteRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByAirbnbId(Long airbnbId);

}