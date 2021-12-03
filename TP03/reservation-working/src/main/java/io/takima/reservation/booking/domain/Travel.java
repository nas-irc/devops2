package io.takima.reservation.booking.domain;

import io.takima.reservation.search.domain.Trip;
import io.takima.reservation.tickets.domain.Ticket;
import io.takima.reservation.trains.domain.Seat;
import io.takima.reservation.trains.domain.Train;
import io.takima.reservation.utils.Pair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "travelId")
@Entity
@Table(name = "TRAVEL")
public class Travel implements Serializable {

    @Id
    @Column(name = "TRAVEL_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "travel_id_seq")
    private Long travelId;

    @Column(name = "PRICE_FIRST_CLASS")
    private Float priceFirstClass;

    @Column(name = "PRICE_SECOND_CLASS")
    private Float priceSecondClass;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TRIP_ID")
    private Trip trip;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TRAIN_ID")
    private Train train;

    @OneToMany(mappedBy = "travel")
    private List<Booking> bookings;

    public Travel mergePriceFirstClass(Float priceFirstClass) {
        if (priceFirstClass != null) {
            this.priceFirstClass = priceFirstClass;
        }

        return this;
    }

    public Travel mergePriceSecondClass(Float priceSecondClass) {
        if (priceSecondClass != null) {
            this.priceSecondClass = priceSecondClass;
        }

        return this;
    }

    public Travel mergeTrain(Train train) {
        if (train != null) {
            this.train = train;
        }

        return this;
    }

    // Returns null if no train has been assigned
    public Pair<Integer, Integer> getAvailableSeatCount() {
        if (train == null) {
            return null;
        }

        var seatCount = train.getSeatCount();

        var firstClassSeatCount = seatCount.getFirst();
        var secondClassSeatCount = seatCount.getSecond();

        var bookedSeatCount = Booking.getBookedSeatCount(bookings);

        var bookedFirstClassSeatCount = bookedSeatCount.getFirst();
        var bookedSecondClassSeatCount = bookedSeatCount.getSecond();

        var availableFirstClassSeatCount = firstClassSeatCount - bookedFirstClassSeatCount;
        var availableSecondClassSeatCount = secondClassSeatCount - bookedSecondClassSeatCount;

        return new Pair<>(availableFirstClassSeatCount, availableSecondClassSeatCount);
    }

    public boolean isSeatBooked(Long seatId) {
        return bookings.stream()
                .flatMap(booking -> booking.getTickets().stream())
                .map(ticket -> ticket.getSeat().getSeatId())
                .anyMatch(id -> id.equals(seatId));
    }

    public boolean isSeatBooked(Seat seat) {
        return bookings.stream()
                .flatMap(booking -> booking.getTickets().stream())
                .map(Ticket::getSeat)
                .anyMatch(seat::equals);
    }
}
