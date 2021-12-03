package io.takima.reservation.tickets.service.impl;

import io.takima.reservation.pdf.inputs.TicketData;
import io.takima.reservation.tickets.domain.Ticket;
import io.takima.reservation.tickets.repository.TicketDao;
import io.takima.reservation.tickets.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    public static final int TICKET_NUMBER_LENGHT = 16;
    private final TicketDao ticketDao;


    @Override
    public String genTicketNumber() {
        var number = "";
        do {
            number = String.join("", UUID.randomUUID().toString().split("-")).substring(0, TICKET_NUMBER_LENGHT).toUpperCase(Locale.ROOT);
        } while (ticketDao.existsTicketByTicketNumberEquals(number));
        return number;
    }

    @Override
    public Ticket create(Ticket ticket) {
        ticket.setTicketNumber(genTicketNumber());
        return ticketDao.save(ticket);
    }


    @Override
    public TicketData getDataForTicker(String number) {
        var ticket = ticketDao.findTicketByTicketNumberEquals(number).orElseThrow(NoSuchElementException::new);
        var car = ticket.getSeat().getCar();
        var hd = ticket.getBooking().getDepartureTimeForStation();
        var ha = ticket.getBooking().getArrivalTimeForStation();

        return TicketData.builder().ticketNumber(ticket.getTicketNumber())
                .lastNamePassenger(ticket.getPassenger().getLastName())
                .firstNamePassenger(ticket.getPassenger().getFirstName())
                .departureDate(ticket.getBooking().getDate())
                .seat(ticket.getSeat().getSeatNumber())
                .carNum(car.getCarId())
                .carclass(car.getCarClass().name())
                .trainId(car.getTrain().getTrainId())
                .departureTime(hd)
                .arrivalTime(ha)
                .stationFrom(ticket.getBooking().getDepartureStation().getName())
                .stationTo(ticket.getBooking().getArrivalStation().getName())
                .trainType(ticket.getBooking().getTravel().getTrip().getTripMotor())
                .build();
    }
}
