package io.takima.reservation.trains.repository;

import io.takima.reservation.trains.domain.Train;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TrainDao {
    /**
     * Gets a page of trains.
     *
     * @param p The parameters of the request.
     * @return A page containing the trains found.
     */
    Page<Train> findAll(Pageable p);

    /**
     * Saves a train in the database.
     *
     * @param train The train to save.
     * @return The train to save.
     */
    Train save(Train train);

    /**
     * Gets a train by it's ID.
     *
     * @param trainId The train ID.
     * @return An optional of a train.
     */
    Optional<Train> findById(Long trainId);
}
