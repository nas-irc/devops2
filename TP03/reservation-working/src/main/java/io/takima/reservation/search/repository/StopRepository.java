package io.takima.reservation.search.repository;

import io.takima.reservation.search.domain.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StopRepository extends JpaRepository<Stop, String> {
    Optional<Stop> findById(String stopId);
}
