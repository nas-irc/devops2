package io.takima.reservation.trains.dto.responses;

import io.takima.reservation.trains.domain.Train;
import lombok.Value;

import java.util.List;

@Value
public class TrainWithAvailability {
    Long trainId;
    List<CarWithAvailability> cars;

    private TrainWithAvailability(Train train, List<CarWithAvailability> cars) {
        this.trainId = train.getTrainId();
        this.cars = cars;
    }

    public static TrainWithAvailability from(Train train, List<CarWithAvailability> cars) {
        if (train == null) {
            return null;
        }

        return new TrainWithAvailability(train, cars);
    }
}
