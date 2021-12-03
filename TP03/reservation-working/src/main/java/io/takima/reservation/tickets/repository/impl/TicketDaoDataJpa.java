package io.takima.reservation.tickets.repository.impl;

import io.takima.reservation.tickets.domain.Ticket;
import io.takima.reservation.tickets.repository.TicketDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketDaoDataJpa extends TicketDao, JpaRepository<Ticket, Long> {
}
