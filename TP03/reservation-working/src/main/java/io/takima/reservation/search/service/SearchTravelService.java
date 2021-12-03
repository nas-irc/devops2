package io.takima.reservation.search.service;

import io.takima.reservation.search.domain.TripType;
import io.takima.reservation.search.domain.views.SearchTravel;
import io.takima.reservation.search.dto.responses.TravelListDto;

import java.time.LocalDate;
import java.util.List;

public interface SearchTravelService {

    /**
     * Fetches all travels that match the set parameters.
     *
     * @param stationFrom The ID of the departure station.
     * @param stationTo   The ID of the arrival station.
     * @param date        The date the travel occurs.
     * @param tripType    The type of travel to look for.
     * @return A list of travels.
     */
    List<TravelListDto> getAllTravels(String stationFrom, String stationTo, LocalDate date, TripType tripType);

    List<SearchTravel> getId(String id);
}
