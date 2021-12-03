package io.takima.reservation.trains.dto.responses;

import io.takima.reservation.trains.domain.Train;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class TrainDto {
    Long trainId;
    List<CarDto> cars;

    private TrainDto(Train train) {
        this.trainId = train.getTrainId();
        this.cars = train.getCars().stream().map(CarDto::fromCar).collect(Collectors.toList());


    }

    public static TrainDto from(Train train) {
        if (train == null) {
            return null;
        }

        return new TrainDto(train);
    }
}
