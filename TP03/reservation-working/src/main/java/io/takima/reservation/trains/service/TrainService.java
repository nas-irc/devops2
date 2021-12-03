package io.takima.reservation.trains.service;

import io.takima.reservation.trains.domain.Train;
import io.takima.reservation.trains.dto.requests.TrainCreationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface TrainService {

    /**
     * Gets a page of trains.
     *
     * @param pageRequest The parameters of the request.
     * @return A page containing the trains found.
     */
    Page<Train> getTrains(PageRequest pageRequest);

    /**
     * Gets a train by it's ID.
     *
     * @param trainId The train ID.
     * @return The train found.
     */
    Train getTrain(long trainId);

    /**
     * Creates a train.
     *
     * @param train              The train to create.
     * @param manualTravelAssign A boolean to toggle the manual assignment of the train to a random travel. Defaults to false.
     * @return The train created.
     */
    Train create(TrainCreationRequest train, boolean manualTravelAssign);
}
