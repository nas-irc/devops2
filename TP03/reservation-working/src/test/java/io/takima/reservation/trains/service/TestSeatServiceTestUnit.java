package io.takima.reservation.trains.service;

import io.takima.reservation.trains.domain.Seat;
import io.takima.reservation.trains.repository.impl.SeatDaoDataJpa;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Unit tests
 */
@ActiveProfiles("test")
@Transactional
@Rollback
@SpringBootTest
public class TestSeatServiceTestUnit {

    @MockBean(name = "seatDaoDataJpa")
    SeatDaoDataJpa seatDao;

    @Autowired
    SeatService seatService;

    @Nested
    class FindSeatByIdTest {

        @Test
        void findSeatById_seatExists_returnsSeat() {
            var seat = Seat.from(1);

            when(seatDao.findById(1)).thenReturn(Optional.of(seat));
            var result = seatService.findSeatById(1);

            assertEquals(seat, result);
        }

        @Test
        void findSeatById_seatNotExists_ThrowsError() {
            assertThrows(NoSuchElementException.class, () ->
                    seatService.findSeatById(1)
            );
        }
    }

    @Nested
    class createTest {


        @Test
        void create_1seat_Returns1Seat() {
            var seat = Seat.from(1);

            when(seatDao.saveAll(List.of(seat))).thenReturn(List.of(seat));
            var result = seatService.create(List.of(seat));

            assertEquals(1, result.size());
            assertEquals(seat, result.get(0));
        }

        @Test
        void create_2seats_Returns2Seats() {
            var seat = Seat.from(1);
            var seat2 = Seat.from(2);

            when(seatDao.saveAll(List.of(seat, seat2))).thenReturn(List.of(seat, seat2));
            var result = seatService.create(List.of(seat, seat2));

            assertEquals(2, result.size());
            assertEquals(seat, result.get(0));
            assertEquals(seat2, result.get(1));
        }
    }
}
