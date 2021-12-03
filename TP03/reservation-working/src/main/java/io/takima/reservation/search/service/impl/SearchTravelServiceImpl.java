package io.takima.reservation.search.service.impl;

import io.takima.reservation.booking.service.TravelService;
import io.takima.reservation.search.domain.TripType;
import io.takima.reservation.search.domain.views.SearchTravel;
import io.takima.reservation.search.dto.responses.TravelListDto;
import io.takima.reservation.search.repository.SearchRepository;
import io.takima.reservation.search.service.SearchTravelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SearchTravelServiceImpl implements SearchTravelService {

    final SearchRepository searchRepo;
    final TravelService travelService;

    @Override
    public List<TravelListDto> getAllTravels(String stationFrom, String stationTo, LocalDate date, TripType tripType) {
        var day = date.getDayOfWeek().toString().toLowerCase();

        return searchRepo.findTravels(stationFrom, stationTo, date, day).stream()
                .map(travel -> TravelListDto.from(travel, stationFrom, stationTo))
                .collect(Collectors.toList());

    }

    @Override
    public List<SearchTravel> getId(String id) {
        return searchRepo.findAllByTripIdEquals(id);
    }

}
