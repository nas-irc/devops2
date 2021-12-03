package io.takima.reservation.booking.service;

import io.takima.reservation.booking.domain.Travel;
import io.takima.reservation.booking.dto.requests.TravelUpdateRequest;
import io.takima.reservation.booking.dto.responses.TravelDetail;
import io.takima.reservation.trains.domain.Train;

import java.util.List;

public interface TravelService {


    List<Travel> getAllTravels();

    Travel getTravel(Long id);

    Travel updateTravel(Long travelId, TravelUpdateRequest request);

    Travel assignRandomTravel(Train train);

    TravelDetail getTravelDetail(Long id, String stationFrom, String stationTo);

    Travel getRandom();

}
