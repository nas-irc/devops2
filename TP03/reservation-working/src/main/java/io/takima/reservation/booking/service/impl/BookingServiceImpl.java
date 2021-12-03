package io.takima.reservation.booking.service.impl;

import io.takima.reservation.booking.domain.Booking;
import io.takima.reservation.booking.domain.Travel;
import io.takima.reservation.booking.dto.requests.BookingRequest;
import io.takima.reservation.booking.dto.responses.BookingResponse;
import io.takima.reservation.booking.repository.BookingDao;
import io.takima.reservation.booking.service.BookingService;
import io.takima.reservation.booking.service.PassengerService;
import io.takima.reservation.booking.service.TravelService;
import io.takima.reservation.exception.InvalidInputException;
import io.takima.reservation.invoices.domain.Invoice;
import io.takima.reservation.search.domain.StopTime;
import io.takima.reservation.tickets.domain.Ticket;
import io.takima.reservation.tickets.service.TicketService;
import io.takima.reservation.trains.domain.CarClass;
import io.takima.reservation.trains.service.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private static final int BOOKING_ID_LENGHT = 10;
    private final BookingDao bookingDao;
    private final TravelService travelService;
    private final PassengerService passengers;
    private final SeatService seats;
    private final TicketService ticketService;

    private final DecimalFormat priceFormater = new DecimalFormat("0.00");


    private void checkSeatsAvailability(Travel travel, List<BookingRequest.SeatBookingRequest> requests) {
        Predicate<BookingRequest.SeatBookingRequest> isAlreadyBooked = r -> travel.isSeatBooked(r.getSeatId());

        var alreadyBookedSeats = requests.stream()
                .filter(isAlreadyBooked)
                .collect(Collectors.toList());

        if (!alreadyBookedSeats.isEmpty()) {
            String alreadyBookedIds = alreadyBookedSeats.stream()
                    .map(BookingRequest.SeatBookingRequest::getSeatId)
                    .map(Object::toString).collect(Collectors.joining(", "));
            throw new InvalidInputException(String.format("Seat(s) with ids %s are already booked", alreadyBookedIds));
        }
    }

    private Booking createBooking(Booking booking) {
        return bookingDao.save(booking);
    }

    private String genBookingNumber() {
        String bookingNumber;
        do {
            bookingNumber = String.join("", UUID.randomUUID().toString().split("-")).substring(0, BOOKING_ID_LENGHT).toUpperCase(Locale.ROOT);
        } while (bookingDao.existsBookingByBookingNumberEquals(bookingNumber));
        return bookingNumber;
    }

    @Transactional
    @Override
    public BookingResponse bookTravel(BookingRequest request) {
        if (request == null) {
            throw new InvalidInputException("Booking request must not be null");
        }
        if (request.hasMissingField()) {
            throw new InvalidInputException("Booking request has missing field(s)");
        }


        var travel = travelService.getTravel(request.getTravelId());

        checkSeatsAvailability(travel, request.getSeatBookingRequests());

        var stopTimes = travel.getTrip().getStopTimes();

        Function<String, StopTime> getStationPoint = stopAreaId -> stopTimes
                .stream()
                .filter(stopTime -> stopTime.getStop()
                        .getParentStation()
                        .equals(stopAreaId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("No stop with parent station %s", stopAreaId)));

        var sA = getStationPoint.apply(request.getStationA()).getStop();
        var sB = getStationPoint.apply(request.getStationB()).getStop();

        var bookingNumber = genBookingNumber();
        var booking = new Booking();

        booking.setTravel(travel);
        booking.setDate(request.getDate());
        booking.setArrivalStation(sB);
        booking.setDepartureStation(sA);

        booking.setBookingNumber(bookingNumber);

        var distance = travel.getTrip().getTotalDistance(sA, sB);

        request.getSeatBookingRequests().forEach(tripper -> {

                    var voyager = passengers.findOrCreatePasssenger(tripper.getFirstName(),
                            tripper.getLastName(),
                            tripper.getBirthdate(),
                            tripper.getEmail(),
                            tripper.getPhoneNumber());
                    var choosenSeat = seats.findSeatById(tripper.getSeatId());

                    var ticket = new Ticket();
                    ticket.setBooking(booking);
                    var tnum = ticketService.genTicketNumber();
            ticket.setPaidPrice(priceFormater.format(distance * (choosenSeat.getCar().getCarClass().equals(CarClass.A) ?
                    travel.getPriceFirstClass() :
                    travel.getPriceSecondClass())));

                    ticket.setTicketNumber(tnum);
                    choosenSeat.addTicket(ticket);
                    ticket.setSeat(choosenSeat);
                    voyager.addBooking(booking);
                    voyager.addTicket(ticket);
                    booking.addTicket(ticket);

                }

        );

        var invoice = new Invoice();
        invoice.setBooking(booking);

        invoice.setTotalPrice(priceFormater.format(booking.getTickets().stream().map(Ticket::getPaidPrice)
                .map(price -> price.replace(',', '.'))
                .map(Float::parseFloat).reduce(0.0f, Float::sum)));

        booking.setInvoice(invoice);
        createBooking(booking);

        return getBookingFor(bookingNumber);
    }

    @Override
    public BookingResponse getBookingFor(String bookingNumber) {
        bookingNumber = bookingNumber.toUpperCase(Locale.ROOT);
        var bookingTickets = bookingDao.getTicketsForABooking(bookingNumber)
                .stream().map(Ticket::getTicketNumber).collect(Collectors.toList());
        return new BookingResponse(bookingNumber, bookingTickets);

    }


}
