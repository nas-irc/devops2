package io.takima.reservation.trains.repository.impl;

import io.takima.reservation.trains.domain.Car;
import io.takima.reservation.trains.repository.CarDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarDaoDataJpa extends CarDao, JpaRepository<Car, Long> {
}
