package io.takima.reservation.booking.repository;

import io.takima.reservation.booking.domain.Travel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TravelDao {

    List<Travel> findAll();

    Travel save(Travel travel);

    Page<Travel> findAll(Pageable p);

    Optional<Travel> findById(Long id);

    long count();
}
