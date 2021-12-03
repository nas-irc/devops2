package io.takima.reservation.search.repository;

import io.takima.reservation.booking.domain.Travel;
import io.takima.reservation.search.domain.views.SearchTravel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface SearchRepository extends JpaRepository<SearchTravel, SearchTravel.SearchKey> {

    List<SearchTravel> findAllByTripIdEquals(String id);


//    /**
//     * Find all travels between stationFrom and stationTo
//     *
//     * @param stationFrom stopArea where begin user trip
//     * @param stationTo   stopArea where end user trip
//     * @param date        date of trip
//     * @param day         dayName of date
//     * @return id 's travel corresponding to search params
//     */
//    @Query("Select travel From Travel travel  where travel.travelId  in ( SELECT ta.travelId  FROM SearchTravel ta " +
//            "inner join SearchTravel tb on ta.tripId=tb.tripId " +
//            "where ta.parentStation=:stationFrom and tb.parentStation=:stationTo " +
//            "and ta.dayName=:day and :date between ta.startDate and ta.endDate and ta.stopOrder < tb.stopOrder )")

    @Query("Select distinct travel, trip, train, route From Travel travel " +
            "join SearchTravel ta on ta.travelId=travel.travelId " +
            "join Trip trip on trip.id = travel.trip.id " +
            "join Train train on train.trainId = travel.train.trainId " +
            "join Route route on trip.route.routeId = route.routeId " +
            "join SearchTravel tb on ta.tripId=tb.tripId " +
            "where ta.parentStation=:stationFrom and tb.parentStation=:stationTo " +
            "and ta.dayName=:day and :date between ta.startDate and ta.endDate and ta.stopOrder < tb.stopOrder")
    Set<Travel> findTravels(String stationFrom, String stationTo, LocalDate date, String day);
}
