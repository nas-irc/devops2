package io.takima.reservation.booking.service;

import io.takima.reservation.booking.dto.requests.BookingRequest;
import io.takima.reservation.booking.dto.responses.BookingResponse;


public interface BookingService {
    BookingResponse bookTravel(BookingRequest request);

    BookingResponse getBookingFor(String bookingNumber);

}
