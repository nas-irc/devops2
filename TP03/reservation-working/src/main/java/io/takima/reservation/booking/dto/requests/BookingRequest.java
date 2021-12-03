package io.takima.reservation.booking.dto.requests;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookingRequest {
    Long travelId;

    List<SeatBookingRequest> seatBookingRequests;

    LocalDate date;
    String stationA;
    String stationB;


    public boolean hasMissingField() {
        return travelId == null || seatBookingRequests == null ||
                seatBookingRequests.isEmpty() || stationA == null || stationA.isEmpty() || stationB == null || stationB.isEmpty() ||
                date == null;
    }

    @Getter
    @Builder
    public static class SeatBookingRequest {
        Long seatId;

        String firstName;

        String lastName;

        String birthdate;

        String email;

        String phoneNumber;
    }
}
