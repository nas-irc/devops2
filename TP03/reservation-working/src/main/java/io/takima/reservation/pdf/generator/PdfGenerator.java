package io.takima.reservation.pdf.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface PdfGenerator<D> {

    /**
     * @param data all data mandatory for generating file
     * @return Path where is saved new file
     * @throws IOException
     */
    String generatePdf(D data) throws IOException;

    /**
     * Construct path where can be saved the file
     *
     * @param number number identifying data
     * @return Path of the file
     */
    String getPathFor(String number);

    /**
     * @param number
     * @return true if the file is already present
     */
    default boolean existsFileFor(String number) {

        return Files.exists(Path.of(getPathFor(number)));

    }

}
