package io.takima.reservation.invoices.service.impl;

import io.takima.reservation.booking.repository.BookingDao;
import io.takima.reservation.invoices.domain.Invoice;
import io.takima.reservation.invoices.repository.InvoiceDao;
import io.takima.reservation.invoices.service.InvoiceService;
import io.takima.reservation.pdf.inputs.InvoiceData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final BookingDao bookings;
    private InvoiceDao invoiceDao;

    @Override
    public Invoice create(Invoice invoice) {
        return invoiceDao.save(invoice);
    }


    @Override
    public InvoiceData getDataForBooking(String bookingNumber) {
        var invoice = bookings.getBookingByBookingNumberEquals(bookingNumber).getInvoice();


        var hd = invoice.getBooking().getDepartureTimeForStation();
        var ha = invoice.getBooking().getArrivalTimeForStation();


        var listInvoicesPassenger = invoice.getBooking().getTickets().stream().map(ticket ->
                InvoiceData.InvoicePassenger.builder()
                        .firstName(ticket.getPassenger()
                                .getFirstName())
                        .classe(ticket.getSeat().getCar().getCarClass().name())
                        .ticketNumber(ticket.getTicketNumber())
                        /* TODO.ticketUrl(ticketPDFGenerator.generatePdf())*/
                        .price(ticket.getPaidPrice())
                        .lastName(ticket.getPassenger().getLastName()).build())
                .collect(Collectors.toList());

        return InvoiceData.builder()
                .bookingNumber(invoice.getBooking().getBookingNumber())
                .trainId(invoice.getBooking().getTravel().getTrain().getTrainId())
                .arrivalTime(ha)
                .travelTime(invoice.getBooking().getDate())
                .departureTime(hd)
                .trainType(invoice.getBooking().getTravel().getTrip().getTripMotor())
                .stationFrom(invoice.getBooking().getDepartureStation().getName())
                .stationTo(invoice.getBooking().getArrivalStation().getName())
                .passengers(listInvoicesPassenger)
                .totalPrice(invoice.getTotalPrice())
                .build();
    }
}
