package io.takima.reservation.trains.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the necessary information to update a train.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainUpdateRequest {

    /**
     * The ID of the travel to assign the train to.
     */
    private Long travelId;

}
