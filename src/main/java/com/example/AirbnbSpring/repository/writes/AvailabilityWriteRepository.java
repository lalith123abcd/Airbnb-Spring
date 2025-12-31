package com.example.AirbnbSpring.repository.writes;

import com.example.AirbnbSpring.model.Availability;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilityWriteRepository extends JpaRepository<Availability, Long> {

    List<Availability> findByBookingId(Long bookingId);

    List<Availability> findByAirbnbId(Long airbnbId);

    List<Availability> findByAirbnbIdAndDateBetween(
            Long airbnbId,
            LocalDate startDate,
            LocalDate endDate
    );


    // SELECT COUNT(*) FROM availability WHERE airbnb_id = airbnbdId AND date BETWEEN startDate AND endDate AND booking_id IS NOT NULL;
    Long countByAirbnbIdAndDateBetweenAndBookingIdIsNotNull(Long airbnbId, LocalDate startDate, LocalDate endDate);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Availability a
        SET a.bookingId = :bookingId
        WHERE a.airbnbId = :airbnbId
        AND a.date BETWEEN :startDate AND :endDate
    """)
    int updateBookingIdByAirbnbIdAndDateBetween(
            @Param("bookingId") Long bookingId,
            @Param("airbnbId") Long airbnbId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
