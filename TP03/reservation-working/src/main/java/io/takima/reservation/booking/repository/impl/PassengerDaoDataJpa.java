package io.takima.reservation.booking.repository.impl;

import io.takima.reservation.booking.domain.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerDaoDataJpa extends JpaRepository<Passenger, Long> {


    Optional<Passenger> findPassengerByBirthDateAndEmailAndFirstName(String birthdate, String email, String fname);


}
