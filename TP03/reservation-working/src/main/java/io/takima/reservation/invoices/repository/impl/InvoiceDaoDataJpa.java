package io.takima.reservation.invoices.repository.impl;

import io.takima.reservation.invoices.domain.Invoice;
import io.takima.reservation.invoices.repository.InvoiceDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceDaoDataJpa extends InvoiceDao, JpaRepository<Invoice, Long> {
}
