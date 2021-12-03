package io.takima.reservation.booking.domain;

import io.takima.reservation.tickets.domain.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"firstName", "birthDate", "email"})
@Entity
@Table(name = "PASSENGER")
public class Passenger implements Serializable {
    /**
     * A passenger will be identified with a combination of firstname-birthdate-mail
     * why ? :
     * - case 1 : Mom => twins underage sons (same email, same birthdate, same lastname ; same phone number )  -> need index on firstname
     * - case 2 : granpa & granny with same ungendered firstname & only one email  =>  (same email, same lastname ,same firstname) -> need index birthdate
     */
    // todo mail & birthdate & firstname index
    //TODO constraints


    @Id
    @Column(name = "PASSENGER_ID")
    @SequenceGenerator(name = "passenger_id_seq", sequenceName = "passenger_id_seq")
    @GeneratedValue(strategy = SEQUENCE, generator = "passenger_id_seq")
    private Long passengerId;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "LASTNAME")
    private String lastName;

    @Column(name = "BIRTHDATE")
    private String birthDate;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @OneToMany(mappedBy = "passenger")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "passenger")
    private List<Ticket> tickets;


    /**
     * Add a booking for this passenger
     *
     * @param booking A booking to add
     */
    public void addBooking(Booking booking) {
        if (bookings == null) {
            bookings = new ArrayList<>();
        }
        booking.setPassenger(this);
        bookings.add(booking);

    }

    /**
     * Add a ticket for this passenger
     *
     * @param ticket A ticket to add
     */
    public void addTicket(Ticket ticket) {
        if (tickets == null) {
            tickets = new ArrayList<>();
        }
        ticket.setPassenger(this);
        tickets.add(ticket);

    }


}
