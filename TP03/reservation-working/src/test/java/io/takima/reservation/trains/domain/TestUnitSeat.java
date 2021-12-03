package io.takima.reservation.trains.domain;


import io.takima.reservation.booking.domain.Booking;
import io.takima.reservation.booking.domain.Travel;
import io.takima.reservation.tickets.domain.Ticket;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
public class TestUnitSeat {

    @Nested
    class AddTicketTest {

        @Test
        void addTicket_emptyList_CreateListAndAddTicket() {
            var seat = new Seat();
            var ticket = Ticket.builder().ticketId(1L).ticketNumber("ABC").build();

            assertNull(seat.getTickets());

            seat.addTicket(ticket);

            assertEquals(1, seat.getTickets().size());
            assertEquals(ticket, seat.getTickets().get(0));
        }

        @Test
        void addTicket_1ItemInList_AddTicketToList() {
            var ticket1 = Ticket.builder().ticketId(1L).ticketNumber("ABC").build();
            var ticket2 = Ticket.builder().ticketId(2L).ticketNumber("DEF").build();
            var listTicket = new ArrayList<Ticket>();
            listTicket.add(ticket1);
            var seat = Seat.builder().tickets(listTicket).build();

            assertEquals(1, seat.getTickets().size());

            seat.addTicket(ticket2);

            assertEquals(2, seat.getTickets().size());
            assertEquals(ticket1, seat.getTickets().get(0));
            assertEquals(ticket2, seat.getTickets().get(1));
        }
    }

    @Nested
    class IsBookedTest {

        @Test
        void isBooked_travelNotBooked_ReturnsFalse() {
            var seat = Seat.builder().seatId(1L).build();
            var travel = Travel.builder().bookings(List.of()).build();

            assertFalse(seat.isBooked(travel));
        }

        @Test
        void isBooked_travelBookedNotThisSeat_ReturnsFalse() {
            var seat1 = Seat.builder().seatId(1L).build();
            var seat2 = Seat.builder().seatId(2L).build();
            var ticket1 = Ticket.builder().ticketId(1L).ticketNumber("ABC").seat(seat2).build();
            var booking = Booking.builder().tickets(List.of(ticket1)).build();
            var travel = Travel.builder().bookings(List.of(booking)).build();

            assertFalse(seat1.isBooked(travel));
        }

        @Test
        void isBooked_travelBookedThisSeat_ReturnsTrue() {
            var seat1 = Seat.builder().seatId(1L).build();
            var ticket1 = Ticket.builder().ticketId(1L).ticketNumber("ABC").seat(seat1).build();
            var booking = Booking.builder().tickets(List.of(ticket1)).build();
            var travel = Travel.builder().bookings(List.of(booking)).build();

            assertTrue(seat1.isBooked(travel));
        }
    }

    @Nested
    class fromTest {

        @Test
        void from_seatNumber_ReturnsSeatWithSameNumber() {
            assertEquals(1, Seat.from(1).getSeatNumber());
        }
    }
}
