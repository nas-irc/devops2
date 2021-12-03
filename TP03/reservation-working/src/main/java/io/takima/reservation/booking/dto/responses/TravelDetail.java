package io.takima.reservation.booking.dto.responses;

import io.takima.reservation.booking.domain.Travel;
import io.takima.reservation.trains.dto.responses.TrainWithAvailability;
import lombok.Value;

@Value
public class TravelDetail {

    Long travelId;
    Float priceFirstClass;
    Float priceSecondClass;
    String tripId;
    TrainWithAvailability train;

    int availableFirstClassSeatCount;
    int availableSecondClassSeatCount;

    private TravelDetail(Travel travel, TrainWithAvailability train, float priceFirstClass, float priceSecondClass) {
        this.travelId = travel.getTravelId();
        this.priceFirstClass = priceFirstClass;
        this.priceSecondClass = priceSecondClass;
        this.tripId = travel.getTrip().getId();
        this.train = train;
        var seatCounts = travel.getAvailableSeatCount();
        this.availableFirstClassSeatCount = seatCounts == null ? 0 : seatCounts.getFirst();
        this.availableSecondClassSeatCount = seatCounts == null ? 0 : seatCounts.getSecond();
    }

    public static TravelDetail from(Travel travel, TrainWithAvailability train, float priceFirstClass, float priceSecondClass) {
        if (travel == null) {
            return null;
        }

        return new TravelDetail(travel, train, priceFirstClass, priceSecondClass);
    }
}
