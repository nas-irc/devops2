package io.takima.reservation.invoices.repository;

import io.takima.reservation.invoices.domain.Invoice;

public interface InvoiceDao {

    Invoice save(Invoice invoice);
}
