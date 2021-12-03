package io.takima.reservation.booking.presentation;

import io.takima.reservation.booking.dto.requests.BookingRequest;
import io.takima.reservation.booking.dto.responses.BookingResponse;
import io.takima.reservation.booking.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/bookings", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class BookingAPI {

    private BookingService bookingService;


    @PostMapping("")
    public ResponseEntity<BookingResponse> bookTravel(@RequestBody BookingRequest request) {


        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.bookTravel(request));
    }

    @GetMapping("")
    public ResponseEntity<BookingResponse> getBooking(@RequestParam("bookingNumber") String bookingNumber) {
        return ResponseEntity.ok(bookingService.getBookingFor(bookingNumber));
    }
}
