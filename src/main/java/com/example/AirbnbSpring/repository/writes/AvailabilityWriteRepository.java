package com.example.AirbnbSpring.repository.writes;

import com.example.AirbnbSpring.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilityWriteRepository extends JpaRepository<Availability,Long> {

    List<Availability> findByBookingId(Long bookingId);

    List<Availability> findByAirbnbId(Long airbnbId);

    List<Availability> findByAirbnbIdAndDateBetween(Long airbnbId, LocalDate startDate,LocalDate endDate);

    // SELECT COUNT(*) FROM availability WHERE airbnb_id = airbnbdId AND date BETWEEN startDate AND endDate AND booking_id IS NOT NULL;

    Long countByAirbnbIdAndDateBetweenAndBookingIdIsNotNull(Long airbnbId, LocalDate startDate, LocalDate endDate);
}
