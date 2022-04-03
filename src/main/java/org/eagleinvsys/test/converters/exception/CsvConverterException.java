package org.eagleinvsys.test.converters.exception;

public class CsvConverterException extends RuntimeException {
    public CsvConverterException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
