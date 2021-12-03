package io.takima.reservation.trains.service;


import io.takima.reservation.exception.InvalidInputException;
import io.takima.reservation.trains.domain.Car;
import io.takima.reservation.trains.domain.CarClass;
import io.takima.reservation.trains.domain.Train;
import io.takima.reservation.trains.dto.requests.CarCreationRequest;
import io.takima.reservation.trains.dto.requests.TrainCreationRequest;
import io.takima.reservation.trains.repository.TrainDao;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Unit tests
 */
@ActiveProfiles("test")
@Transactional
@Rollback
@SpringBootTest
public class TestTrainServiceTestUnit {

    @MockBean(name = "trainDao")
    TrainDao trainDao;

    @Autowired
    TrainService trainService;

    @MockBean(name = "carService")
    private CarService carService;

    @Nested
    class getTrainTest {

        @Test
        void getTrain_trainExists_returnsTrain() {
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

            when(trainDao.findById(1L)).thenReturn(Optional.of(train));

            var result = trainService.getTrain(1L);

            assertEquals(train, result);
        }

        @Test
        void getTrain_trainNotExists_ThrowsError() {
            assertThrows(NoSuchElementException.class, () -> trainService.getTrain(1L));
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

            when(carService.create(trainReq)).thenReturn(List.of(car));
            when(trainDao.save(train)).thenReturn(train);

            var result = trainService.create(trainReq, true);

            assertEquals(train.getCars(), result.getCars());
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


            when(carService.create(trainReq)).thenReturn(List.of(car, car2));
            when(trainDao.save(train)).thenReturn(train);

            var result = trainService.create(trainReq, true);

            assertEquals(train.getCars(), result.getCars());
        }

        @Test
        void createTrain_NoCars_isError() {
            var trainReq = new TrainCreationRequest(List.of());

            assertThrows(InvalidInputException.class, () ->
                    trainService.create(trainReq, true)
            );
        }


    }

    @Nested
    class getTrainsTest {

        @Test
        void getTrains_limit1Train_returns1Train() {
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
            PageRequest pageRequest = PageRequest.of(0, 1);
            Page<Train> page = new PageImpl(List.of(train));

            when(trainDao.findAll(pageRequest)).thenReturn(page);

            var result = trainService.getTrains(pageRequest);

            assertEquals(1, result.getTotalElements());
            assertTrue(page.stream().anyMatch(tr -> tr.equals(train)));
        }
    }
}
