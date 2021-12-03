package io.takima.reservation.trains.dto.responses;

import io.takima.reservation.trains.domain.Seat;
import lombok.Value;

/**
 * Represents a seat with information about it's availability.
 */
@Value
public class SeatWithAvailability {

    /**
     * The seat ID.
     */
    Long seatId;

    /**
     * The seat number.
     */
    Integer seatNumber;

    /**
     * The seat availability.
     */
    boolean available;

    /**
     * Returns a SeatWithAvailability from a seat.
     *
     * @param seat      The seat to get information from.
     * @param available The seat availability.
     * @return The DTO.
     */
    public static SeatWithAvailability from(Seat seat, boolean available) {
        return new SeatWithAvailability(seat.getSeatId(), seat.getSeatNumber(), available);
    }
}
