package com.example.AirbnbSpring.saga;

import com.example.AirbnbSpring.services.handlers.AvailabilityEventHandler;
import com.example.AirbnbSpring.services.handlers.BookingEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SagaEventProcessor {
    private final BookingEventHandler bookingEventHandler;
    private final AvailabilityEventHandler availabilityEventHandler;


    public void processEvent(SagaEvent sagaEvent) {
        log.info("🧭 SagaEventProcessor received eventType={}", sagaEvent.getEventType());



        switch (sagaEvent.getEventType()){
            case "BOOKING_CREATED":
                break;
            case "BOOKING_CONFIRM_REQUESTED":
                bookingEventHandler.handleBookingConfirmRequested(sagaEvent);
                break;
            case "BOOKING_CONFIRMED":
                availabilityEventHandler.handleBookingConfirmed(sagaEvent);

                break;
            case "BOOKING_CANCEL_REQUESTED":
                bookingEventHandler.handleBookingCancelRequested(sagaEvent);
                break;
            case "BOOKING_CANCELLED":
                availabilityEventHandler.handleBookingCancelled(sagaEvent);
                break;
            case "BOOKING_COMPENSATED":
                break;
            default:
                break;
        }
    }

}