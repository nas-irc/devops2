package io.takima.reservation.trains.service;

import io.takima.reservation.trains.domain.Seat;

import java.util.List;

public interface SeatService {


    /**
     * Gets a seat by it's ID.
     *
     * @param seatId The seat ID.
     * @return The seat found.
     */
    Seat findSeatById(long seatId);

    /**
     * Creates several seats.
     *
     * @param seats A list of seats.
     * @return The list of seats created.
     */
    List<Seat> create(List<Seat> seats);
}
