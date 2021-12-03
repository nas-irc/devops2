package io.takima.reservation.booking.dto.responses;


import lombok.Value;

import java.util.List;


@Value
public class BookingResponse {
    String bookingNumber;
    List<String> tickets;
}