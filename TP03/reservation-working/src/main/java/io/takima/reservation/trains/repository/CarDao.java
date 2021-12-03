package io.takima.reservation.trains.repository;

import io.takima.reservation.trains.domain.Car;

import java.util.Optional;

public interface CarDao {

    /**
     * Create a car.
     *
     * @param car The car to create.
     * @return The car to create.
     */
    Car save(Car car);

    /**
     * Gets a car by it's ID.
     *
     * @param carId The car ID.
     * @return An optional of a car.
     */
    Optional<Car> findById(Long carId);
}
