package io.takima.reservation.booking.repository.impl;

import io.takima.reservation.booking.domain.Travel;
import io.takima.reservation.booking.repository.TravelDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelDaoDataJpa extends TravelDao, JpaRepository<Travel, Long> {
}
