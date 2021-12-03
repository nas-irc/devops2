package io.takima.reservation.tickets.domain;

import io.takima.reservation.booking.domain.Booking;
import io.takima.reservation.booking.domain.Passenger;
import io.takima.reservation.trains.domain.Seat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "ticketId")
@Entity
@Table(name = "TICKET")
public class Ticket implements Serializable {

    @Id
    @Column(name = "TICKET_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_id_seq")
    Long ticketId;

    @Column(name = "TICKET_NUMBER")
    String ticketNumber;

    @Column(name = "PAID_PRICE")
    String paidPrice;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SEAT_ID")
    Seat seat;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PASSENGER_ID")
    Passenger passenger;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BOOKING_ID")
    Booking booking;


}
