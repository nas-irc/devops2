package io.takima.reservation.tickets;


import io.takima.reservation.pdf.generator.TicketPDFGenerator;
import io.takima.reservation.tickets.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping(value = "/api/tickets")
@AllArgsConstructor
public class TicketController {


    private final TicketService tickets;
    private final TicketPDFGenerator ticketPDFGenerator;

    @GetMapping(value = "/{ticketNumber}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getTicketWithNumber(@PathVariable(name = "ticketNumber") String number) throws IOException {
        var url = "";
        if (ticketPDFGenerator.existsFileFor(number)) {
            url = ticketPDFGenerator.getPathFor(number);
        } else {
            var data = tickets.getDataForTicker(number);
            url = ticketPDFGenerator.generatePdf(data);
        }

        try (var in = new FileInputStream(url)) {
            return ResponseEntity.ok(in.readAllBytes());
        }

    }

}
