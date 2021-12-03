package io.takima.reservation.search.dto.responses;

import io.takima.reservation.search.domain.StopTime;
import lombok.Value;

import java.time.LocalTime;

@Value
public class StopTimeDto {

    LocalTime departure;

    String name;

    private StopTimeDto(StopTime stopTime) {
        this.departure = stopTime.getDeparture();
        this.name = stopTime.getStop().getName();
    }

    public static StopTimeDto from(StopTime stopTime) {
        if (stopTime == null) {
            return null;
        }
        return new StopTimeDto(stopTime);
    }
}
