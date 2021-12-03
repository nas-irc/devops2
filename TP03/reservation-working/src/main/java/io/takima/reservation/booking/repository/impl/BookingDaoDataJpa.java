package io.takima.reservation.booking.repository.impl;

import io.takima.reservation.booking.domain.Booking;
import io.takima.reservation.booking.repository.BookingDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingDaoDataJpa extends BookingDao, JpaRepository<Booking, Long> {


}
