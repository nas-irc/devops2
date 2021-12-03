package io.takima.reservation.trains.service.impl;

import io.takima.reservation.exception.InvalidInputException;
import io.takima.reservation.trains.domain.Seat;
import io.takima.reservation.trains.repository.SeatDao;
import io.takima.reservation.trains.service.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatDao seatsRepository;

    @Override
    public Seat findSeatById(long seatId) {
        return seatsRepository.findById(seatId).orElseThrow(() -> new NoSuchElementException(String.format("No seat with id %d", seatId)));
    }

    @Override
    public List<Seat> create(List<Seat> seats) {
        if (seats.isEmpty()) {
            throw new InvalidInputException("The list of seats should not be empty");
        }
        var seatIt = seatsRepository.saveAll(seats);
        return StreamSupport.stream(seatIt.spliterator(), false)
                .collect(Collectors.toList());
    }


}
