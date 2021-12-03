package io.takima.reservation.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalTime;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Immutable
@NoArgsConstructor
@EqualsAndHashCode(of = {"trip", "stop"})
@Entity
@IdClass(StopTime.Key.class)
@Table(name = "STOP_TIMES")
public class StopTime implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "TRIP_ID")
    Trip trip;

    @Id
    @ManyToOne
    @JoinColumn(name = "STOP_ID")
    Stop stop;

    @Column(name = "ARRIVAL_TIME")
    LocalTime arrival;

    @Column(name = "DEPARTURE_TIME")
    LocalTime departure;

    @Column(name = "STOP_SEQUENCE")
    Integer sequence;

    @Column(name = "STOP_HEADSIGN")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String headSign;

    @Column(name = "PICKUP_TYPE")
    @JsonIgnore
    Integer pickupType;

    @Column(name = "DROP_OFF_TYPE")
    @JsonIgnore
    Integer dropOffType;

    @Column(name = "SHAPE_DIST_TRAVELED")
    @JsonIgnore
    String shapeDistTravel;

    @Data
    @NoArgsConstructor
    public static class Key implements Serializable {
        private Trip trip;
        private Stop stop;
    }
}
