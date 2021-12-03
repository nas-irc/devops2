package io.takima.reservation.trains.repository.impl;

import io.takima.reservation.trains.domain.Seat;
import io.takima.reservation.trains.repository.SeatDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatDaoDataJpa extends SeatDao, JpaRepository<Seat, Long> {
}
