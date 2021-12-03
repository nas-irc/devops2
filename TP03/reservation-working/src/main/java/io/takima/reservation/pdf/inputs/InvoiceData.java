package io.takima.reservation.pdf.inputs;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
public class InvoiceData {
    String bookingNumber;
    List<InvoicePassenger> passengers;
    LocalDate travelTime;
    long trainId;
    String stationFrom;
    String stationTo;
    LocalTime departureTime;
    LocalTime arrivalTime;
    String trainType;
    String totalPrice;


    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @Builder
    @Getter
    public static class InvoicePassenger {
        String firstName;
        String lastName;
        String ticketNumber;
        String ticketUrl;
        String price;
        String classe;


    }
}
