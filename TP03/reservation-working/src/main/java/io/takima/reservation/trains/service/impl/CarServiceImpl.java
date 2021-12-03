package io.takima.reservation.trains.service.impl;

import io.takima.reservation.exception.InvalidInputException;
import io.takima.reservation.trains.domain.Car;
import io.takima.reservation.trains.domain.Seat;
import io.takima.reservation.trains.dto.requests.CarCreationRequest;
import io.takima.reservation.trains.dto.requests.TrainCreationRequest;
import io.takima.reservation.trains.repository.CarDao;
import io.takima.reservation.trains.service.CarService;
import io.takima.reservation.trains.service.SeatService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    CarDao carDao;
    SeatService seatService;

    @Override
    public Car getCar(Long carId) {
        return carDao.findById(carId)
                .orElseThrow(() -> new NoSuchElementException(String.format("No Car with id %d", carId)));
    }

    private Car create(Car car) {
        return carDao.save(car);
    }

    private Car createCar(CarCreationRequest request) {
        var carClass = request.getCarClass();
        var seatCount = 0;

        if (carClass.equals("A")) {
            seatCount = request.getNumberSeatFirstClass();
        } else if (carClass.equals("B")) {
            seatCount = request.getNumberSeatSecondClass();
        } else {
            throw new InvalidInputException(String.format("Invalid Car class %s", carClass));
        }

        var seats = IntStream.rangeClosed(1, seatCount)
                .mapToObj(Seat::from)
                .collect(Collectors.toList());

        seats = seatService.create(seats);

        var car = Car.shallowFrom(request);

        car.setSeats(seats);

        car = create(car);

        var finalCar = car;
        seats.forEach(seat -> seat.setCar(finalCar));

        return car;
    }

    @Override
    public List<Car> create(TrainCreationRequest request) {

        return request.getCars()
                .stream()
                .map(this::createCar)
                .collect(Collectors.toList());
    }
}
