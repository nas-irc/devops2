package io.takima.reservation.trains.domain;

import io.takima.reservation.booking.domain.Travel;
import io.takima.reservation.tickets.domain.Ticket;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "seatId")
@Entity
@Table(name = "SEAT")
public class Seat implements Serializable {

    /**
     * The seat ID.
     */
    @Id
    @Column(name = "SEAT_ID")
    @SequenceGenerator(name = "seat_id_seq", sequenceName = "seat_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seat_id_seq")
    Long seatId;

    /**
     * The seat number.
     */
    @Column(name = "SEAT_NUMBER")
    int seatNumber;

    /**
     * The car the seat belongs to.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CAR_ID")
    Car car;

    /**
     * The tickets that booked this seat.
     */
    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    public static Seat from(int seatNumber) {
        return new Seat(null, seatNumber, null, new ArrayList<>());
    }

    /**
     * Checks if the seat is booked for a set travel.
     *
     * @param travel The travel to check.
     * @return true if the seat is booked, false otherwise.
     */
    public boolean isBooked(Travel travel) {
        return travel.getBookings()
                .stream()
                .flatMap(booking -> booking.getTickets().stream())
                .map(Ticket::getSeat)
                .anyMatch(seat -> seat.seatId.equals(this.seatId));
    }

    /**
     * Adds a ticket to the list of tickets.
     *
     * @param t The ticket to add.
     */
    public void addTicket(Ticket t) {
        if (this.tickets == null) {
            tickets = new ArrayList<>();
        }
        t.setSeat(this);
        tickets.add(t);


    }
}
