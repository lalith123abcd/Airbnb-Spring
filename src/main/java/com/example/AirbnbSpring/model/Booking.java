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
    private String id;

    @Column(nullable = false)
    @ManyToOne
    private String userId;

    @Column(nullable = false)
    @ManyToMany
    private String airbnbId;

    private String totalPrice;

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
