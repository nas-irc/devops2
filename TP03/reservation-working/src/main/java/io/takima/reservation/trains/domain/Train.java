package io.takima.reservation.trains.domain;

import io.takima.reservation.utils.Pair;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "trainId")
@Entity
@Table(name = "TRAIN")
@ToString
public class Train implements Serializable {

    /**
     * The train ID.
     */
    @Id
    @Column(name = "TRAIN_ID")
    @SequenceGenerator(name = "train_id_seq", sequenceName = "train_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "train_id_seq")
    Long trainId;

    /**
     * The cars the train contains.
     */
    @OneToMany(mappedBy = "train")
    @ToString.Exclude
    private List<Car> cars;


    public static Train from(List<Car> cars) {
        return new Train(null, cars);
    }

    /**
     * Counts the number of seats in first and second class.
     *
     * @return A pair containing the total number of seats in first and second class.
     */
    public Pair<Integer, Integer> getSeatCount() {

        var carTypeMapToCars = cars
                .stream()
                .collect(Collectors.groupingBy(Car::getCarClass));

        var firstClassSeatCount = carTypeMapToCars.getOrDefault(CarClass.A, new ArrayList<>())
                .stream()
                .mapToInt(Car::getNumberSeatFirstClass)
                .sum();

        var secondClassSeatCount = carTypeMapToCars.getOrDefault(CarClass.B, new ArrayList<>())
                .stream()
                .mapToInt(Car::getNumberSeatSecondClass)
                .sum();

        return new Pair<>(firstClassSeatCount, secondClassSeatCount);
    }
}
