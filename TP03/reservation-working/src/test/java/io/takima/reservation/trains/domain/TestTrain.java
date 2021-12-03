package io.takima.reservation.trains.domain;

import io.takima.reservation.utils.Pair;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
public class TestTrain {

    @Nested
    class getSeatCountTest {

        @Test
        void getSeatCount_emptyCars_Returns0Pair() {
            var train = Train.builder().cars(List.of()).build();
            var pair = new Pair<>(0, 0);

            assertEquals(pair.getFirst(), train.getSeatCount().getFirst());
            assertEquals(pair.getSecond(), train.getSeatCount().getSecond());
        }

        @Test
        void getSeatCount_withSeats_ReturnsPair() {
            var car1 = Car.builder()
                    .numberSeatFirstClass(58)
                    .numberSeatSecondClass(0)
                    .seats(IntStream.rangeClosed(1, 58)
                            .mapToObj(Seat::from)
                            .collect(Collectors.toList()))
                    .carClass(CarClass.A)
                    .build();

            var car2 = Car.builder()
                    .numberSeatFirstClass(0)
                    .numberSeatSecondClass(88)
                    .seats(IntStream.rangeClosed(1, 88)
                            .mapToObj(Seat::from)
                            .collect(Collectors.toList()))
                    .carClass(CarClass.B)
                    .build();

            var train = Train.builder().cars(List.of(car1, car2, car1)).build();
            var pair = new Pair<>(58 * 2, 88);

            assertEquals(pair.getFirst(), train.getSeatCount().getFirst());
            assertEquals(pair.getSecond(), train.getSeatCount().getSecond());
        }


    }
}
