package io.takima.reservation.booking.domain;

import io.takima.reservation.invoices.domain.Invoice;
import io.takima.reservation.search.domain.Stop;
import io.takima.reservation.tickets.domain.Ticket;
import io.takima.reservation.utils.Pair;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "bookingId")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "BOOKING")
public class Booking implements Serializable {

    @Id
    @Column(name = "BOOKING_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_id_seq")
    Long bookingId;


    /**
     * All passengers of a same booking will possess the same booking Number
     */
    @Column(name = "BOOKING_NUMBER")//TODO index ?
            String bookingNumber;

    /**
     * Travel booked
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TRAVEL_ID")
    Travel travel;

    /**
     * Passenger tied to this booking
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PASSENGER_ID")
    Passenger passenger;

    /**
     * Tickets related to it
     */
    @OneToMany(mappedBy = "booking")
    private List<Ticket> tickets;

    /**
     * Invoice related to this booking
     */
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    Invoice invoice;

    /**
     * Date of the travel tied on the booking
     */
    LocalDate date;

    /**
     * Departure station for this booking
     */
    @JoinColumn(name = "DEPARTURE_STATION")
    @ManyToOne(cascade = CascadeType.ALL)
    Stop departureStation;


    /**
     * Arrival station for this booking
     */
    @JoinColumn(name = "ARRIVAL_STATION")
    @ManyToOne(cascade = CascadeType.ALL)
    Stop arrivalStation;


    /**
     * @param bookings
     * @return
     */
    public static Pair<Integer, Integer> getBookedSeatCount(List<Booking> bookings) {
        var carTypeMapToTickets = bookings.stream()
                .flatMap(booking -> booking.tickets.stream())
                .collect(Collectors.groupingBy(ticket -> ticket.getSeat().getCar().getCarType()));

        var bookedFirstClassSeatCount = carTypeMapToTickets.getOrDefault("A", new ArrayList<>()).size();

        var bookedSecondClassSeatCount = carTypeMapToTickets.getOrDefault("B", new ArrayList<>()).size();

        return new Pair<>(bookedFirstClassSeatCount, bookedSecondClassSeatCount);
    }

    /**
     * Add a ticket to this booking
     *
     * @param ticket
     */
    public void addTicket(Ticket ticket) {
        if (tickets == null) tickets = new ArrayList<>();
        ticket.setBooking(this);
        tickets.add(ticket);

    }

    public LocalTime getDepartureTimeForStation() {
        return this.travel.getTrip()
                .getStopTimes().stream()
                .filter(stopTime -> stopTime.getStop().getId().equals(this.departureStation.getId()))
                .findFirst().orElseThrow(NoSuchElementException::new).getDeparture();
    }

    public LocalTime getArrivalTimeForStation() {
        return this.travel.getTrip()
                .getStopTimes().stream()
                .filter(stopTime -> stopTime.getStop().getId().equals(this.arrivalStation.getId()))
                .findFirst().orElseThrow(NoSuchElementException::new).getArrival();
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", bookingNumber='" + bookingNumber + '\'' +
                ", travel=" + travel.getTravelId() +
                ", passenger=" + passenger.getPassengerId() +
                ", tickets=" + tickets.stream().map(Ticket::getTicketId).collect(Collectors.toList()) +
                ", invoice=" + ((invoice == null) ? " null" : invoice) +
                '}';
    }
}
