package io.takima.reservation.invoices.service;

import io.takima.reservation.invoices.domain.Invoice;
import io.takima.reservation.pdf.inputs.InvoiceData;

public interface InvoiceService {
    Invoice create(Invoice invoice);

    InvoiceData getDataForBooking(String bookingNumber);
}
