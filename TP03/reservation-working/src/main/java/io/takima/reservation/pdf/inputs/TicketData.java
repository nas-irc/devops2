package io.takima.reservation.pdf.inputs;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
public class TicketData {


    String ticketNumber;
    String firstNamePassenger;
    String lastNamePassenger;
    LocalDate departureDate;
    int seat;
    long trainId;
    String carclass;
    long carNum;
    String stationFrom;
    String stationTo;
    LocalTime departureTime;
    LocalTime arrivalTime;
    String trainType;


}
