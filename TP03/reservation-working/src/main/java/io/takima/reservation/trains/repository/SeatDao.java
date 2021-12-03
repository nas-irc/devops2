package io.takima.reservation.trains.repository;

import io.takima.reservation.trains.domain.Seat;

import java.util.Optional;

public interface SeatDao {

    /**
     * Creates several seats.
     *
     * @param var1 An iterable on seats.
     * @return An iterable on the seats created.
     */
    <S extends Seat> Iterable<S> saveAll(Iterable<S> var1);

    /**
     * Gets a seat by it's ID.
     *
     * @param seatId The seat ID.
     * @return An optional of a seat.
     */
    Optional<Seat> findById(long seatId);

}
