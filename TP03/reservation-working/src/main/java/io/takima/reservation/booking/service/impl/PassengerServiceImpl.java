package io.takima.reservation.booking.service.impl;

import io.takima.reservation.booking.domain.Passenger;
import io.takima.reservation.booking.repository.impl.PassengerDaoDataJpa;
import io.takima.reservation.booking.service.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;


@AllArgsConstructor
@Service
public class PassengerServiceImpl implements PassengerService {

    private PassengerDaoDataJpa passengers;

    private String toNameCase(String str) {
        str = str.trim();
        return str.substring(0, 1).toUpperCase() +
                str.substring(1).toLowerCase(Locale.ROOT);
    }

    private String parseTelNumber(String str) {
        str = str.replace("[^0-9]", "");
        return str;
    }

    @Override
    public Passenger findOrCreatePasssenger(String firstName, String lastName, String birthdate, String email, String phoneNumber) {
        var cleanMail = email.strip().toLowerCase(Locale.ROOT);
        var cleanBirthdate = birthdate.strip().replace("/", "-");
        var cleanFirstName = toNameCase(firstName);
        var cleanTelNumber = parseTelNumber(phoneNumber);
        var optPassenger = passengers.findPassengerByBirthDateAndEmailAndFirstName(cleanBirthdate, cleanMail, cleanFirstName);

        return optPassenger.orElseGet(() -> passengers.save(Passenger.builder()
                .lastName(toNameCase(lastName))
                .firstName(cleanFirstName)
                .birthDate(cleanBirthdate)
                .email(cleanMail)
                .phoneNumber(cleanTelNumber)
                .build()));
    }

}
