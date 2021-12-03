package io.takima.reservation.pdf.generator;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import io.takima.reservation.pdf.inputs.InvoiceData;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

@NoArgsConstructor
@Service
public class InvoiceGenerator implements PdfGenerator<InvoiceData> {

    private static final float SIZE_TEXT_IN_CELL = 11.0f;
    private static final float DEFAULT_SIZE = 15.0f;
    private static final String FONT_NAME = StandardFonts.HELVETICA;
    private static final String OUTPUT_PATH = "files/invoices/";
    private static final String EXTENSION = ".pdf";
    private static final String LOGO_PATH = "files/statics/takicon.png";
    private static final float LOGO_SIZE = 100.0f;


    @Override
    public String generatePdf(InvoiceData data) throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_PATH));
        var url = getPathFor(data.getBookingNumber());
        var font = PdfFontFactory.createFont(FONT_NAME);


        PdfCanvas canvasLogo;
        var pdfDoc = new PdfDocument(new PdfWriter(url));
        var pageSize = PageSize.A4;

        try (var doc = new Document(pdfDoc, pageSize)) {
            canvasLogo = new PdfCanvas(pdfDoc.addNewPage());

            var logo = ImageDataFactory.create(LOGO_PATH);
            var rectLogo = new Rectangle(16.0f, 16.0f, LOGO_SIZE + 5, LOGO_SIZE + 7);
            canvasLogo.addImageFittedIntoRectangle(logo, rectLogo, false);

            var titre = new Text("FACTURE \nRESERVATION n° " + data.getBookingNumber()).setFont(font)
                    .setFontColor(ColorConstants.LIGHT_GRAY)
                    .setFontSize(DEFAULT_SIZE).setBold();

            var titreCase = new Paragraph(titre);

            var namesCols = new String[]{"#", "Nom", "Classe", "Prix", "Billet"};
            var sizeCols = new float[]{1.0f, 6.0f, 2.0f, 5.0f, 8.0f};

            var table = new Table(UnitValue.createPercentArray(sizeCols));
            var cell = new Cell(1, namesCols.length)
                    .add(new Paragraph("Passagers"))
                    .setFont(font)
                    .setFontSize(SIZE_TEXT_IN_CELL)
                    .setFontColor(DeviceGray.WHITE)
                    .setBackgroundColor(DeviceGray.BLACK)
                    .setTextAlignment(TextAlignment.CENTER);


            table.addHeaderCell(cell);

            for (var colName : namesCols) {
                var cellHeaderI = new Cell();
                cellHeaderI.setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(SIZE_TEXT_IN_CELL).add(new Paragraph(new Text(colName)));
                table.addHeaderCell(cellHeaderI);
            }
            var index = 0;
            for (var passengerData : data.getPassengers()) {
                var cellI = new Cell();
                var nameCell = new Cell();
                var classCell = new Cell();
                var priceCell = new Cell();
                var ticketCell = new Cell();

                var pName = passengerData.getFirstName().toUpperCase(Locale.ROOT).charAt(0) + ". " + passengerData.getLastName();

                cellI.add(new Paragraph(new Text("" + ++index).setFontSize(SIZE_TEXT_IN_CELL)));
                nameCell.add(new Paragraph(new Text(pName).setFontSize(SIZE_TEXT_IN_CELL)));
                classCell.add(new Paragraph(new Text(passengerData.getClasse()).setFontSize(SIZE_TEXT_IN_CELL).setTextAlignment(TextAlignment.CENTER)));
                priceCell.add(new Paragraph(new Text(passengerData.getPrice() + " €").setFontSize(SIZE_TEXT_IN_CELL)));
                ticketCell.add(new Paragraph(new Text(passengerData.getTicketNumber()).setFontSize(SIZE_TEXT_IN_CELL)));

                table.addCell(cellI);
                table.addCell(nameCell);
                table.addCell(classCell);
                table.addCell(priceCell);
                table.addCell(ticketCell);
            }
            table.addFooterCell(new Cell());
            table.addFooterCell(new Cell());
            table.addFooterCell(new Cell());
            table.addFooterCell(data.getTotalPrice() + "  €");
            doc.add(titreCase);
            doc.add(table);

        }

        return url;
    }

    @Override
    public String getPathFor(String number) {
        return OUTPUT_PATH + number + EXTENSION;
    }


}
