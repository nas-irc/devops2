package io.takima.reservation.trains.presentation;

import io.takima.reservation.trains.dto.requests.TrainCreationRequest;
import io.takima.reservation.trains.dto.responses.TrainDto;
import io.takima.reservation.trains.service.TrainService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * The train API allows to perfom Create, Reach operations on trains.
 */
@RestController
@RequestMapping(value = "/api/trains", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class TrainApi {

    /**
     * A train service.
     */
    private TrainService trainService;

    /**
     * Returns a page of trains.
     *
     * @param page The page number. Defaults to 0.
     * @param size The number of trains to put in the page. Defaults to 20.
     * @return A ResponseEntity of a page of Train.
     */
    @GetMapping("")
    public ResponseEntity<Page<TrainDto>> getAllTrains(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                       @RequestParam(name = "size", required = false, defaultValue = "20") int size) {

        var pageRequest = PageRequest.of(page, size, Sort.by("trainId"));

        var trains = trainService.getTrains(pageRequest).map(TrainDto::from);

        return ResponseEntity.ok(trains);
    }

    /**
     * Get a train by it's ID.
     *
     * @param trainId The train ID.
     * @return The train found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrainDto> getTrain(@PathVariable(name = "id") Long trainId) {
        var train = trainService.getTrain(trainId);

        var trainDto = TrainDto.from(train);

        return ResponseEntity.ok(trainDto);
    }

    /**
     * Creates a train.
     *
     * @param train              The train to create.
     * @param manualTravelAssign A boolean to toggle the manual assignment of the train to a random travel. Defaults to false.
     * @return The train created.
     */
    @PostMapping("")
    public ResponseEntity<TrainDto> createTrain(@RequestBody TrainCreationRequest train, @RequestParam(required = false, defaultValue = "false") boolean manualTravelAssign) {

        var trainDto = TrainDto.from(trainService.create(train, manualTravelAssign));

        return ResponseEntity.status(HttpStatus.CREATED).body(trainDto);
    }
}
