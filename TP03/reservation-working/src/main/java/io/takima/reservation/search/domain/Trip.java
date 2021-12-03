package io.takima.reservation.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.takima.reservation.booking.domain.Travel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "TRIPS")
public class Trip implements Serializable {

    @Id
    @Column(name = "TRIP_ID")
    String id;

    @ManyToOne
    @JoinColumn(name = "ROUTE_ID")
    Route route;

    @Column(name = "SERVICE_ID")
    @JsonIgnore
    Integer serviceId;

    @Column(name = "TRIP_HEADSIGN")
    Integer headSign;

    @Column(name = "DIRECTION_ID")
    @JsonIgnore
    String direction;

    @Column(name = "BLOCK_ID")
    @JsonIgnore
    String block;

    @Column(name = "SHAPE_ID")
    @JsonIgnore
    String shape;

    @OneToOne(mappedBy = "trip", optional = false)
    @NotNull
    @JsonIgnore
    Travel travel;

    @OneToMany(mappedBy = "trip")
    @JsonIgnore
    private List<StopTime> stopTimes;

    @Column(name = "TRIP_MOTOR")
    String tripMotor;

    @Override
    public String toString() {
        return "Trip{" +
                "id='" + id + '\'' +
                ", travel=" + travel.getTravelId() +
                '}';
    }


    private double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    private double distanceTwoContiguousStations(Stop stationA, Stop stationB) {
        var earthRadiusKm = 6371;

        var dLat = degreesToRadians(stationB.getLatitude() - stationA.getLatitude());
        var dLon = degreesToRadians(stationB.getLongitude() - stationA.getLongitude());

        var lat1 = degreesToRadians(stationA.getLatitude());
        var lat2 = degreesToRadians(stationB.getLatitude());

        var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);

        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusKm * c;
    }

    public float getTotalDistance(Stop stFrom, Stop stTo) {


        var stops = stopTimes.stream().sorted(Comparator.comparingInt(StopTime::getSequence))
                .collect(Collectors.toList());


        var idx = new int[]{
                IntStream.range(0, stops.size())
                        .filter(i -> stops.get(i).getStop().equals(stFrom))
                        .findAny()
                        .orElseThrow(NoSuchElementException::new),
                IntStream.range(0, stops.size())
                        .filter(i -> stops.get(i).getStop().equals(stTo))
                        .findAny()
                        .orElseThrow(NoSuchElementException::new)
        };
        Arrays.sort(idx);
        var trueTravel = stops.subList(idx[0], idx[1] + 1);


        var distance = 0.0;
        var currentStation = stFrom;


        for (var i = 1; i < trueTravel.size(); i++) {
            distance += distanceTwoContiguousStations(currentStation, trueTravel.get(i).getStop());
            currentStation = trueTravel.get(i).getStop();
        }
        return (float) distance;
    }

}
