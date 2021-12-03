package io.takima.reservation.trains.presentation;

import io.takima.reservation.exception.InvalidInputException;
import io.takima.reservation.trains.domain.Car;
import io.takima.reservation.trains.domain.CarClass;
import io.takima.reservation.trains.domain.Train;
import io.takima.reservation.trains.dto.requests.CarCreationRequest;
import io.takima.reservation.trains.dto.requests.TrainCreationRequest;
import io.takima.reservation.trains.service.TrainService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

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
public class TestTrainApiTestUnit {

    @MockBean
    TrainService trainService;

    @Autowired
    TrainApi trainApi;

    @Nested
    class getTrainTest {

        @Test
        void getTrain_trainExists_isOk() {
            var train = Train.builder().trainId(1L).build();

            var carList = List.of(Car.builder()
                    .carId(1L)
                    .carClass(CarClass.A)
                    .carType("VTU")
                    .numberSeatFirstClass(58)
                    .numberSeatSecondClass(0)
                    .train(train)
                    .build());

            train.setCars(carList);

            when(trainService.getTrain(1L)).thenReturn(train);

            var result = trainApi.getTrain(1L);

            assertEquals(HttpStatus.OK, result.getStatusCode());
        }

        @Test
        void getTrain_trainNotExists_ThrowsError() {
            when(trainService.getTrain(1L)).thenThrow(new NoSuchElementException("No train with id 1"));

            assertThrows(NoSuchElementException.class, () -> trainApi.getTrain(1L));
        }
    }

    @Nested
    class createTrainTest {

        @Test
        void createTrain_1Car_isCreated() {
            var carReq1 = new CarCreationRequest("VTU", "A", 58, 0);
            var trainReq = new TrainCreationRequest(List.of(carReq1));

            var train = Train.builder().trainId(1L).build();

            var car = Car.builder()
                    .carId(1L)
                    .carClass(CarClass.A)
                    .carType("VTU")
                    .numberSeatFirstClass(58)
                    .numberSeatSecondClass(0)
                    .train(train)
                    .build();

            train.setCars(List.of(car));

            when(trainService.create(trainReq, false)).thenReturn(train);

            var result = trainApi.createTrain(trainReq, false);

            assertEquals(HttpStatus.CREATED, result.getStatusCode());
        }

        @Test
        void createTrain_2Cars_isCreated() {
            var carReq1 = new CarCreationRequest("VTU", "A", 58, 0);
            var trainReq = new TrainCreationRequest(List.of(carReq1, carReq1));

            var train = Train.builder().trainId(1L).build();

            var car = Car.builder()
                    .carId(1L)
                    .carClass(CarClass.A)
                    .carType("VTU")
                    .numberSeatFirstClass(58)
                    .numberSeatSecondClass(0)
                    .train(train)
                    .build();

            var car2 = Car.builder()
                    .carId(2L)
                    .carClass(CarClass.A)
                    .carType("VTU")
                    .numberSeatFirstClass(58)
                    .numberSeatSecondClass(0)
                    .train(train)
                    .build();

            train.setCars(List.of(car, car2));

            when(trainService.create(trainReq, false)).thenReturn(train);

            var result = trainApi.createTrain(trainReq, false);

            assertEquals(HttpStatus.CREATED, result.getStatusCode());
        }

        @Test
        void createTrain_NoCars_isError() {
            var trainReq = new TrainCreationRequest(List.of());

            when(trainService.create(trainReq, false)).thenThrow(new InvalidInputException());

            assertThrows(InvalidInputException.class, () ->
                    trainApi.createTrain(trainReq, false)
            );
        }

        @Test
        void createTrain_Null_isError() {
            when(trainService.create(null, false)).thenThrow(new InvalidInputException());

            assertThrows(InvalidInputException.class, () ->
                    trainApi.createTrain(null, false)
            );
        }
    }
}
