package io.takima.reservation.trains.dto.responses;

import io.takima.reservation.trains.domain.Car;
import lombok.Value;

@Value
public class CarDto {

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
    String carClass;

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


    private CarDto(Car car) {
        this.carId = car.getCarId();
        this.carType = car.getCarType();
        this.carClass = car.getCarClass().name();
        this.numberSeatFirstClass = car.getNumberSeatFirstClass();
        this.numberSeatSecondClass = car.getNumberSeatSecondClass();
        this.trainId = car.getTrain().getTrainId();
    }

    /**
     * Returns a CarDTO from a Car.
     *
     * @param car The car to get information from.
     * @return The DTO.
     */
    public static CarDto fromCar(Car car) {
        return new CarDto(car);
    }
}
