package io.takima.reservation.invoices.domain;

import io.takima.reservation.booking.domain.Booking;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "invoiceId")
@Entity
@Table(name = "INVOICE")
public class Invoice implements Serializable {

    @Column(name = "TOTAL_PRICE")
    String totalPrice;
    @Id
    @Column(name = "INVOICE_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_id_seq")
    private Long invoiceId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BOOKING_ID")
    private Booking booking;

}
