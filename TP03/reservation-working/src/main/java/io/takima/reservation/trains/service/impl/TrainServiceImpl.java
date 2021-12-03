package io.takima.reservation.trains.service.impl;

import io.takima.reservation.booking.service.TravelService;
import io.takima.reservation.exception.InvalidInputException;
import io.takima.reservation.trains.domain.Train;
import io.takima.reservation.trains.dto.requests.TrainCreationRequest;
import io.takima.reservation.trains.repository.TrainDao;
import io.takima.reservation.trains.service.CarService;
import io.takima.reservation.trains.service.TrainService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Service
public class TrainServiceImpl implements TrainService {

    TrainDao trainDao;

    CarService carService;

    TravelService travelService;


    public Page<Train> getTrains(PageRequest pageRequest) {
        return trainDao.findAll(pageRequest);
    }

    @Override
    public Train getTrain(long trainId) {
        return trainDao.findById(trainId).orElseThrow(() -> new NoSuchElementException(String.format("No Train with id %d", trainId)));
    }

    @Override
    public Train create(TrainCreationRequest request, boolean manualTravelAssign) {
        if (request.getCars() == null || request.getCars().isEmpty()) {
            throw new InvalidInputException("Train creation request is missing the cars");
        }

        var cars = carService.create(request);

        var train = Train.from(cars);

        trainDao.save(train);

        var finalTrain = train;
        cars.forEach(car -> car.setTrain(finalTrain));


        if (!manualTravelAssign && !travelService.getAllTravels().isEmpty()) {

            travelService.assignRandomTravel(trainDao.save(finalTrain));
        }

        return train;
    }
}
