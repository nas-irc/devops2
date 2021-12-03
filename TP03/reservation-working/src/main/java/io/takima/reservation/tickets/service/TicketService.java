package io.takima.reservation.tickets.service;

import io.takima.reservation.pdf.inputs.TicketData;
import io.takima.reservation.tickets.domain.Ticket;


public interface TicketService {

    String genTicketNumber();

    Ticket create(Ticket ticket);

    TicketData getDataForTicker(String number);
}
