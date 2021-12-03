package io.takima.reservation.trains.domain;

import io.takima.reservation.trains.dto.requests.CarCreationRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(of = "carId")
@Entity
@Table(name = "CAR")
public class Car implements Serializable {

    /**
     * The car ID.
     */
    @Id
    @Column(name = "CAR_ID")
    @SequenceGenerator(name = "car_id_seq", sequenceName = "car_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_id_seq")
    Long carId;

    /**
     * The car's seats layout.
     * The values can either be "VU" or "VTU".
     */
    @Column(name = "CAR_TYPE")
    String carType;

    /**
     * The class of the car.
     * The values can either be "A" or "B".
     */
    @Column(name = "CAR_CLASS")
    @Enumerated(value = EnumType.STRING)
    CarClass carClass;

    /**
     * The number of first class seats in the car.
     * The values can either be 0 or 58.
     */
    @Column(name = "NUMBER_SEAT_FIRST_CLASS")
    int numberSeatFirstClass;

    /**
     * The number of second class seats in the car.
     * The values can either be 0 or 88.
     */
    @Column(name = "NUMBER_SEAT_SECOND_CLASS")
    int numberSeatSecondClass;

    /**
     * The train the car belongs to.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TRAIN_ID")
    Train train;

    /**
     * The seats the car contains.
     */
    @OneToMany(mappedBy = "car")
    private List<Seat> seats;

    public static Car shallowFrom(CarCreationRequest request) {
        return new Car(null, request.getCarType(), CarClass.valueOf(request.getCarClass()), request.getNumberSeatFirstClass(), request.getNumberSeatSecondClass(), null, new ArrayList<>());
    }
}
