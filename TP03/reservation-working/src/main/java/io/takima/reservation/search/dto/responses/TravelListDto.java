package io.takima.reservation.search.dto.responses;

import io.takima.reservation.booking.domain.Travel;
import io.takima.reservation.search.domain.StopTime;
import lombok.Value;

import java.util.NoSuchElementException;
import java.util.function.Function;

@Value
public class TravelListDto {

    Long id;
    Float priceFirstClass;
    Float priceSecondClass;
    int availableFirstClassSeatCount;
    int availableSecondClassSeatCount;


    StopTimeDto departure;
    StopTimeDto arrival;


    private TravelListDto(Travel travel, String stationFrom, String stationTo) {
        this.id = travel.getTravelId();

        var seatsAvailable = travel.getAvailableSeatCount();


        this.availableFirstClassSeatCount = seatsAvailable == null ? 0 : seatsAvailable.getFirst();
        this.availableSecondClassSeatCount = seatsAvailable == null ? 0 : seatsAvailable.getSecond();


        var stopTimes = travel.getTrip().getStopTimes();

        /* Looks for a stopTime in the trip's stopTime list */
        Function<String, StopTime> lookForStation = stp -> stopTimes.stream()
                .filter(stopTime -> stopTime
                        .getStop()
                        .getParentStation()
                        .equals(stp))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("no such station with id %s", stp)));

        this.departure = StopTimeDto.from(lookForStation.apply(stationFrom));
        this.arrival = StopTimeDto.from(lookForStation.apply(stationTo));
        var distance = travel.getTrip().getTotalDistance(lookForStation.apply(stationFrom).getStop(), lookForStation.apply(stationTo).getStop());
        this.priceFirstClass = travel.getPriceFirstClass() * distance;
        this.priceSecondClass = travel.getPriceSecondClass() * distance;

    }

    public static TravelListDto from(Travel travel, String stationFrom, String stationTo) {
        if (travel == null) {
            return null;
        }

        return new TravelListDto(travel, stationFrom, stationTo);
    }


}
