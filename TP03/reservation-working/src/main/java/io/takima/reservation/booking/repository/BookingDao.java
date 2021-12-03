package io.takima.reservation.booking.repository;

import io.takima.reservation.booking.domain.Booking;
import io.takima.reservation.tickets.domain.Ticket;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingDao {
    Booking save(Booking booking);

    boolean existsBookingByBookingNumberEquals(String bookingNuber);

    Booking getBookingByBookingNumberEquals(String bookingNumber);

    @Query("select b.tickets from Booking b where b.bookingNumber = :bookingNumber")
    List<Ticket> getTicketsForABooking(String bookingNumber);
}
