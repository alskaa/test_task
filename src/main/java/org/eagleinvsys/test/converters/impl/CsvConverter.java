package org.eagleinvsys.test.converters.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.eagleinvsys.test.converters.Converter;
import org.eagleinvsys.test.converters.ConvertibleCollection;
import org.eagleinvsys.test.converters.exception.CsvConverterException;
import org.eagleinvsys.test.converters.util.ConvertibleCollectionMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;


public class CsvConverter implements Converter {

    /**
     * Converts given {@link ConvertibleCollection} to CSV and outputs result as a text to the provided {@link OutputStream}
     *
     * @param collectionToConvert collection to convert to CSV format
     * @param outputStream        output stream to write CSV conversion result as text to
     */
    @Override
    public void convert(ConvertibleCollection collectionToConvert, OutputStream outputStream) {
        OutputStreamWriter out = new OutputStreamWriter(outputStream);

        CSVFormat.Builder csvFormatBuilder = CSVFormat.Builder.create(CSVFormat.DEFAULT);

        if (collectionToConvert.getHeaders() != null && !collectionToConvert.getHeaders().isEmpty()) {
            csvFormatBuilder.setHeader(
                    collectionToConvert.getHeaders().toArray(new String[0])
            );
        }

        CSVFormat csvFormat = csvFormatBuilder.build();

        try (CSVPrinter printer = new CSVPrinter(out, csvFormat)) {

            printer.printRecords(ConvertibleCollectionMapper.recordsToIterable(collectionToConvert));

        } catch (IOException e) {

            throw new CsvConverterException("CSV conversion failed", e);
        }

    }
}