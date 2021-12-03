package io.takima.reservation.trains.service;

import io.takima.reservation.trains.domain.Car;
import io.takima.reservation.trains.domain.CarClass;
import io.takima.reservation.trains.dto.requests.CarCreationRequest;
import io.takima.reservation.trains.dto.requests.TrainCreationRequest;
import io.takima.reservation.trains.repository.CarDao;
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
public class TestCarServiceTestUnit {

    @MockBean(name = "carDao")
    CarDao carDao;

    @MockBean
    SeatService seatService;

    @Autowired
    private CarService carService;


    @Nested
    class getCarTest {

        @Test
        void getCar_carExists_returnsCar() {
            var car = Car.builder()
                    .carId(1L)
                    .carClass(CarClass.A)
                    .carType("VTU")
                    .numberSeatFirstClass(58)
                    .numberSeatSecondClass(0)
                    .build();

            when(carDao.findById(1L)).thenReturn(Optional.of(car));
            var result = carService.getCar(1L);
            assertEquals(car, result);
        }

        @Test
        void getCar_carNotExists_ThrowsError() {
            assertThrows(NoSuchElementException.class, () ->
                    carService.getCar(1L)
            );
        }
    }

    @Nested
    class createTest {

        @Test
        void create_1Car_Returns1Car() {
            var carReq1 = new CarCreationRequest("VTU", "A", 58, 0);
            var trainReq = new TrainCreationRequest(List.of(carReq1));

            var car = Car.builder()
                    .carClass(CarClass.A)
                    .carType("VTU")
                    .numberSeatFirstClass(58)
                    .numberSeatSecondClass(0)
                    .seats(List.of())
                    .build();

            when(carDao.save(car)).thenReturn(car);

            var result = carService.create(trainReq);

            assertEquals(1, result.size());
            assertEquals(car, result.get(0));
        }

        @Test
        void create_2Car_Returns2Cars() {
            var carReq1 = new CarCreationRequest("VTU", "A", 58, 0);
            var trainReq = new TrainCreationRequest(List.of(carReq1, carReq1));

            var car = Car.builder()
                    .carClass(CarClass.A)
                    .carType("VTU")
                    .numberSeatFirstClass(58)
                    .numberSeatSecondClass(0)
                    .seats(List.of())
                    .build();

            when(carDao.save(car)).thenReturn(car);

            var result = carService.create(trainReq);

            assertEquals(2, result.size());
            assertEquals(car, result.get(0));
            assertEquals(car, result.get(1));
        }
    }


}
