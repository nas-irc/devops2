package io.takima.reservation.trains.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents the necessary information to create a train.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainCreationRequest {

    /**
     * The list of cars to create.
     */
    private List<CarCreationRequest> cars;
}
