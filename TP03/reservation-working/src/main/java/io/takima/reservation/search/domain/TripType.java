package io.takima.reservation.search.domain;


import java.util.stream.Stream;

public enum TripType {

    BOTH("both"),
    DIRECT("direct"),
    CORRESPONDENCE("correspondence");

    private final String value;

    TripType(String tripType) {
        this.value = tripType;
    }

    public static TripType fromString(String tripType) {

        return Stream.of(TripType.values())
                .filter(val -> val.value.equalsIgnoreCase(tripType))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("tripType not found : " + tripType));
    }
}
