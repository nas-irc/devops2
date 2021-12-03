package io.takima.reservation.tickets.repository;

import io.takima.reservation.tickets.domain.Ticket;

import java.util.Optional;

public interface TicketDao {


    Ticket save(Ticket ticket);

    Optional<Ticket> findTicketByTicketNumberEquals(String number);

    boolean existsTicketByTicketNumberEquals(String ticketNumber);
}
