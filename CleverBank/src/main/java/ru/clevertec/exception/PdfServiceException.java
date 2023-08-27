package ru.clevertec.exception;

public class PdfServiceException extends IllegalStateException {
    public PdfServiceException(String s) {
        super(s);
    }
}