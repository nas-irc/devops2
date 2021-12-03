package io.takima.reservation.search.presentation;

import io.takima.reservation.search.domain.TripType;
import io.takima.reservation.search.dto.responses.TravelListDto;
import io.takima.reservation.search.service.SearchTravelService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/travels", produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchTravelApi {

    private final SearchTravelService searchService;

    @GetMapping("/search")
    public ResponseEntity<List<TravelListDto>> searchAllTravels(@RequestParam(value = "from") String stationFrom, @RequestParam(value = "to") String stationTo,
                                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                                @RequestParam(required = false, value = "tripType", defaultValue = "both") String tripTypeStr) {

        var tripType = TripType.fromString(tripTypeStr);

        var travels = searchService.getAllTravels(stationFrom, stationTo, date, tripType);


        return ResponseEntity.ok(travels);
    }

}
