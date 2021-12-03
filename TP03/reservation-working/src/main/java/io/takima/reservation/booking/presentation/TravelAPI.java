package io.takima.reservation.booking.presentation;

import io.takima.reservation.booking.dto.requests.TravelUpdateRequest;
import io.takima.reservation.booking.dto.responses.TravelDetail;
import io.takima.reservation.booking.service.TravelService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/travels", produces = MediaType.APPLICATION_JSON_VALUE)
public class TravelAPI {

    private TravelService travelService;

    @GetMapping("/{id}")
    public ResponseEntity<TravelDetail> getTravel(@PathVariable Long id, @RequestParam String stationFrom, @RequestParam String stationTo) {

        return ResponseEntity.ok(travelService.getTravelDetail(id, stationFrom, stationTo));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateTravel(@PathVariable Long id, @RequestBody TravelUpdateRequest request) {
        travelService.updateTravel(id, request);

        return ResponseEntity.ok().build();
    }
}
