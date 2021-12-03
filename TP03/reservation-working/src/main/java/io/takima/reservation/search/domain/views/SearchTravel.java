package io.takima.reservation.search.domain.views;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entity (imutable mapping SearchTavel SQL view
 */
@Entity
@Immutable
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@IdClass(SearchTravel.SearchKey.class)
@EqualsAndHashCode(of = {"tripId", "stopId"})
@Table(name = "VIEW_TRAVELS_SEARCH")
public class SearchTravel implements Serializable {

    @Id
    String tripId;
    @Id
    String stopId;

    LocalDate startDate;

    LocalDate endDate;

    String stopName;

    LocalTime departureTime;

    LocalTime arrivalTime;

    int stopOrder;

    @Column(name = "dayname")
    String dayName;

    long travelId;

    @Column(name = "trip_type")
    String tripType;

    String parentStation;


    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class SearchKey implements Serializable {
        private String tripId;
        private String stopId;
    }

}
