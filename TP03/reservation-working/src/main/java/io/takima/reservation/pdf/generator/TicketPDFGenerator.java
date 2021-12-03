package io.takima.reservation.pdf.generator;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import io.takima.reservation.pdf.inputs.TicketData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class TicketPDFGenerator implements PdfGenerator<TicketData> {
    public static final float NAME_SIZE = 15.0f;
    public static final float LOGOSIZE = 80.0f;
    private static final float DEFAULT_SIZE = 15.0f;
    private static final String FONT_NAME = StandardFonts.HELVETICA;
    private static final float SEAT_SIZE = 20.0f;
    private static final float TRAIN_FULL_DETAILS_SIZE = 14.0f;
    private static final float DEPART_SIZE = DEFAULT_SIZE;
    private static final float CLASS_SIZE = DEFAULT_SIZE;
    private static final float TICKET_NUMBER_SIZE = DEFAULT_SIZE;
    private static final DateTimeFormatter DATE_DEPARTURE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Color TEXT_COLOR = ColorConstants.DARK_GRAY;
    private static final String IMG_BACKGROUND_PATH = "files/statics/bg_ticket.png";
    private static final String LOGO_PATH = "files/statics/takicon.png";
    private static final String OUTPUT_PATH = "files/tickets/";
    private static final String EXTENSION = ".pdf";
    private static final DateTimeFormatter ARRIVAL_DEPARTURE_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Modifiez à vos riques et périls
     */
    @Override
    public String generatePdf(TicketData data) throws IOException {
        String resPath = getPathFor(data.getTicketNumber());


        Files.createDirectories(Paths.get(OUTPUT_PATH));

        var trainSeat = data.getTrainType() + " - " + data.getTrainId() + " - " + data.getCarNum() + " - " + data.getSeat();
        trainSeat = trainSeat.toUpperCase(Locale.ROOT);
        var font = PdfFontFactory.createFont(FONT_NAME);

        var pdfDoc = new PdfDocument(new PdfWriter(resPath));
        var pageSize = new PageSize(595.0f, 280.0f);

        try (var doc = new Document(pdfDoc, pageSize)) {
            var imageBg = ImageDataFactory.create(IMG_BACKGROUND_PATH);
            var canvasBg = new PdfCanvas(pdfDoc.addNewPage());
            canvasBg.saveState();

            var state = new PdfExtGState().setFillOpacity(0.25f);
            canvasBg.setExtGState(state);

            var rectBg = new Rectangle(100.0f, 15.0f, pageSize.getWidth() - 115.0f, pageSize.getHeight() - 30.0f);
            canvasBg.addImageFittedIntoRectangle(imageBg, rectBg, false);
            canvasBg.restoreState();

            var canvasLogo = new PdfCanvas(pdfDoc.getFirstPage());
            var logo = ImageDataFactory.create(LOGO_PATH);
            var rectLogo = new Rectangle(5.0f, 7.0f, LOGOSIZE + 5, LOGOSIZE + 7);
            canvasLogo.addImageFittedIntoRectangle(logo, rectLogo, false);

            var nameText = new Text(data.getFirstNamePassenger() + "\n" + data.getLastNamePassenger().toUpperCase(Locale.ROOT))
                    .setFont(font)
                    .setFontColor(TEXT_COLOR)
                    .setFontSize(NAME_SIZE).setBold();

            var nameCase = new Paragraph(nameText).setFixedPosition(110.0f, 215.0f, 80.0f);

            var seatText = new Text("Siège : " + data.getSeat())
                    .setFont(font)
                    .setFontColor(TEXT_COLOR)
                    .setFontSize(SEAT_SIZE);

            var classText = new Text(" Classe : " + data.getCarclass())
                    .setFont(font)
                    .setFontSize(CLASS_SIZE)
                    .setFontColor(ColorConstants.RED);

            var classCase = new Paragraph(classText).setFixedPosition(5.0f, 255.0f, 80.0f);

            var trainText = new Text(trainSeat)
                    .setFont(font)
                    .setFontSize(TRAIN_FULL_DETAILS_SIZE)
                    .setFontColor(TEXT_COLOR);


            var underlineSeat = 20.0f;
            var trainSeatCaseX = 350.0f;
            var trainSeatCaseY = 240.0f;
            var trainCase = new Paragraph(trainText).setFixedPosition(trainSeatCaseX, trainSeatCaseY, 250.0f);

            var seatCase = new Paragraph(seatText).setFixedPosition(trainSeatCaseX + 140, trainSeatCaseY - underlineSeat, 180.0f);
            var dateText = " Le : " + data.getDepartureDate().format(DATE_DEPARTURE_FORMAT);

            var departText = new Text("De : " + data.getStationFrom() + dateText + " à " + data.getDepartureTime().format(ARRIVAL_DEPARTURE_TIME_FORMAT))
                    .setFont(font)
                    .setFontSize(DEPART_SIZE)
                    .setFontColor(TEXT_COLOR);

            var arrivalText = new Text("A   : " + data.getStationTo() + dateText + " à " + data.getArrivalTime().format(ARRIVAL_DEPARTURE_TIME_FORMAT))
                    .setFont(font)
                    .setFontSize(DEPART_SIZE)
                    .setFontColor(TEXT_COLOR);

            var underlinedacase = 20.0f;
            var departureCase = new Paragraph(departText).setFixedPosition(100.0f, 50.0f, 500.0f);


            var arrivalCase = new Paragraph(arrivalText).setFixedPosition(100.0f, 50.0f - underlinedacase, 500.0f);

            var numberText = new Text(data.getTicketNumber())
                    .setFont(font)
                    .setFontSize(TICKET_NUMBER_SIZE)
                    .setFontColor(TEXT_COLOR);

            var ticketNumberCase = new Paragraph(numberText.setTextAlignment(TextAlignment.RIGHT)).setFixedPosition(433.0f, -5.0f, 150.0f);

            doc.add(nameCase);
            doc.add(trainCase);
            doc.add(classCase);
            doc.add(seatCase);
            doc.add(departureCase);
            doc.add(arrivalCase);
            doc.add(ticketNumberCase);


        }


        return resPath;
    }

    @Override
    public String getPathFor(String number) {
        return OUTPUT_PATH + number + EXTENSION;
    }

}
