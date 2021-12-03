package io.takima.reservation.booking.service;

import io.takima.reservation.booking.domain.Passenger;

public interface PassengerService {

    Passenger findOrCreatePasssenger(String firstName, String lastName, String birthdate, String email, String phoneNumber);


}
