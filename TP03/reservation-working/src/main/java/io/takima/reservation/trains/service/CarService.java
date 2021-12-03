package io.takima.reservation.trains.service;

import io.takima.reservation.trains.domain.Car;
import io.takima.reservation.trains.dto.requests.TrainCreationRequest;

import java.util.List;

public interface CarService {

    /**
     * Create several cars.
     *
     * @param request The cars to create.
     * @return The cars created.
     */
    List<Car> create(TrainCreationRequest request);

    /**
     * Gets a car by it's ID.
     *
     * @param carId The car ID.
     * @return The car found.
     */
    Car getCar(Long carId);

}
