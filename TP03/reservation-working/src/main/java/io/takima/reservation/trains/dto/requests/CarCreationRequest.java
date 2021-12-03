package io.takima.reservation.trains.dto.requests;


import lombok.Value;

/**
 * Represents the necessary information to create a car.
 */
@Value
public class CarCreationRequest {

    /**
     * The car's seats layout.
     * The values can either be "VU" or "VTU".
     */
    String carType;

    /**
     * The class of the car.
     * The values can either be "A" or "B".
     */
    String carClass;

    /**
     * The number of first class seats in the car.
     * The values can either be 0 or 58.
     */
    int numberSeatFirstClass;

    /**
     * The number of second class seats in the car.
     * The values can either be 0 or 88.
     */
    int numberSeatSecondClass;
}
