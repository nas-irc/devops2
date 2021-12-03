package io.takima.reservation.booking.service.impl;

import io.takima.reservation.booking.dto.requests.BookingRequest;
import io.takima.reservation.booking.dto.responses.BookingResponse;
import io.takima.reservation.exception.InvalidInputException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("dev")
@Transactional
@AutoConfigureMockMvc
class BookingServiceTest {
    //TODO: fix test with testcontainer
/*
    @Autowired
    BookingServiceImpl service;
    @PersistenceContext
    private EntityManager entityManager;


    @Rollback
    @Test
    void bookTravel_everithing_is_ok() {

        BookingRequest bookingRequest = new BookingRequest(
                1654L, List.of(BookingRequest.SeatBookingRequest.builder()
                .phoneNumber("0123456789")
                .email("mail1@mail.com").birthdate("00/00/0000")
                .seatId(728777L)
                .lastName("Lastname1")
                .firstName("firstname")
                .build(), BookingRequest.SeatBookingRequest.builder()
                .phoneNumber("0123456780")
                .email("mail2@mail.com").birthdate("00/00/0000")
                .seatId(728778L)
                .lastName("Lastname2")
                .firstName("firstname")
                .build()),
                LocalDate.of(2021, 9, 1), "StopArea:OCE87113001", "StopArea:OCE87171009");

        BookingResponse bookingResponse = service.bookTravel(bookingRequest);
        entityManager.flush();
        assertEquals(2, bookingResponse.getTickets().size());
        assertFalse(bookingResponse.getBookingNumber().isEmpty());
    }
        @Test
        void bookTravel_twice_same_seat() {


            BookingRequest bookingRequest = new BookingRequest(
                    1654L, List.of(BookingRequest.SeatBookingRequest.builder()
                    .phoneNumber("0123456789")
                    .email("mail1@mail.com").birthdate("01/00/0000")
                    .seatId(728777L)
                    .lastName("Lastname112")
                    .firstName("firstnae")
                    .build(), BookingRequest.SeatBookingRequest.builder()
                    .phoneNumber("0123456780")
                    .email("mail2@mail.com").birthdate("00/01/0000")
                    .seatId(728778L)
                    .lastName("Lastname2")
                    .firstName("firstname")
                    .build()),
                    LocalDate.of(2021, 9, 1), "StopArea:OCE87113001", "StopArea:OCE87171009");

            BookingResponse bookingResponse = service.bookTravel(bookingRequest);
            entityManager.flush();
            assertThrows(InvalidInputException.class, () -> service.bookTravel(bookingRequest));
        }

    @Test
    void bookingRequestNull() {
        assertThrows(InvalidInputException.class, () -> service.bookTravel(null));
    }
    */
}