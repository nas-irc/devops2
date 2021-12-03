package io.takima.reservation.invoices;

import io.takima.reservation.invoices.service.InvoiceService;
import io.takima.reservation.pdf.generator.InvoiceGenerator;
import io.takima.reservation.pdf.inputs.InvoiceData;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/invoices")
public class InvoiceController {

    private final InvoiceService invoices;
    private final InvoiceGenerator generator;

    @GetMapping(value = "", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getInvoice(@RequestParam String bookingNumber) throws IOException {

        var url = "";

        if (generator.existsFileFor(bookingNumber)) {
            url = generator.getPathFor(bookingNumber);
        } else {
            InvoiceData data = invoices.getDataForBooking(bookingNumber);
            url = generator.generatePdf(data);
        }

        try (InputStream in = new FileInputStream(url)) {


            return ResponseEntity.ok(in.readAllBytes());
        }
    }
}
