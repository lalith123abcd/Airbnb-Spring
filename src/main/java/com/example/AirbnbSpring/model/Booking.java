package com.example.AirbnbSpring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "userId",nullable = false)
    private User user;


    @ManyToOne
    @JoinColumn(name = "airbnbId",nullable = false)
    private Airbnb airbnb;

    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private BookingStatus bookingStatus=BookingStatus.PENDING;

    @Column(unique = true)
    private String idempotencyKey;

    public enum BookingStatus{
        PENDING,CANCELLED,CONFIRMED
    }
}
