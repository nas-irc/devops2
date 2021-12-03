package io.takima.reservation.trains.repository.impl;

import io.takima.reservation.trains.domain.Train;
import io.takima.reservation.trains.repository.TrainDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainDaoDataJpa extends TrainDao, JpaRepository<Train, Long> {
}
