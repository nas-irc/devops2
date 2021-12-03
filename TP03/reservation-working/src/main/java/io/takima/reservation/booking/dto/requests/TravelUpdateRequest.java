package io.takima.reservation.booking.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TravelUpdateRequest {

    private Float priceFirstClass;
    private Float priceSecondClass;
    private Long trainId;

    public boolean isEmpty() {
        return priceFirstClass == null && priceSecondClass == null && trainId == null;
    }
}
