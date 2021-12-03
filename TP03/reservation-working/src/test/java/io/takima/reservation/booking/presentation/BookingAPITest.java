package io.takima.reservation.booking.presentation;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.takima.reservation.booking.dto.requests.BookingRequest;
import io.takima.reservation.booking.dto.responses.BookingResponse;
import io.takima.reservation.booking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Rollback
class BookingAPITest {


    private final String URL = "/api/bookings";
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private BookingService bookingServiceMock;

    private ObjectMapper om = new ObjectMapper();

    private BookingResponse bookingFromScratch(int nPassengers) {
        String ticketNumber = "AAAAAAAAAAAAAAA";
        String bookingNumber = "BBBBBBBBBB";
        List<String> tickets = new ArrayList<>(nPassengers);
        for (int i = 0; i < nPassengers; i++) {
            tickets.add(ticketNumber + i);
        }

        return new BookingResponse(bookingNumber, tickets);
    }


    private void bookingExpt(BookingRequest bookingRequest) {
        BookingResponse bookingResponse = bookingFromScratch(bookingRequest.getSeatBookingRequests().size());
        when(bookingServiceMock.bookTravel(bookingRequest)).thenReturn(bookingResponse);
        when(bookingServiceMock.getBookingFor("bookingNumber")).thenReturn(bookingResponse);

    }

    @Test
    void bookTravel() throws Exception {
        var br = new BookingRequest(1L,
                Collections.singletonList(BookingRequest.SeatBookingRequest.builder()
                        .seatId(1L).birthdate("00/00/0000").firstName("firstname")
                        .lastName("lastname").email("mail@mail.com").phoneNumber("0123456789")
                        .build()), null, "StationFrom", "StationTo");
        bookingExpt(br);
        mockMvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(br)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        assert (true);
    }

    @Test
    void getBooking() throws Exception {
        var br = new BookingRequest(1L,
                Collections.singletonList(BookingRequest.SeatBookingRequest.builder()
                        .seatId(1L).birthdate("00/00/0000").firstName("firstname")
                        .lastName("lastname").email("mail@mail.com").phoneNumber("0123456789")
                        .build()), null, "StationFrom", "StationTo");
        bookingExpt(br);

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("bookingNumber", "bookingNumber").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}