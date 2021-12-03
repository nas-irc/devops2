package io.takima.reservation.trains.dto.responses;

import io.takima.reservation.trains.domain.Car;
import io.takima.reservation.trains.domain.CarClass;
import lombok.Value;

import java.util.List;


/**
 * Represents a car with information about each seat.
 */
@Value
public class CarWithAvailability {

    /**
     * The car ID.
     */
    Long carId;

    /**
     * The car layout.
     */
    String carType;

    /**
     * The car class.
     */
    CarClass carClass;

    /**
     * The number of first class seats.
     */
    int numberSeatFirstClass;

    /**
     * The number of second class seats.
     */
    int numberSeatSecondClass;

    /**
     * The ID of the train the car belongs to.
     */
    Long trainId;

    /**
     * The list of seats the car contains.
     */
    List<SeatWithAvailability> seats;


    private CarWithAvailability(Car car, List<SeatWithAvailability> seats) {
        this.carId = car.getCarId();
        this.carType = car.getCarType();
        this.carClass = car.getCarClass();
        this.numberSeatFirstClass = car.getNumberSeatFirstClass();
        this.numberSeatSecondClass = car.getNumberSeatSecondClass();
        this.seats = seats;
        this.trainId = car.getTrain().getTrainId();
    }

    /**
     * Returns a CarWithAvailability from a car and seats.
     *
     * @param car   The car to get information from.
     * @param seats The seats with availability information.
     * @return The DTO.
     */
    public static CarWithAvailability from(Car car, List<SeatWithAvailability> seats) {
        return new CarWithAvailability(car, seats);
    }
}
