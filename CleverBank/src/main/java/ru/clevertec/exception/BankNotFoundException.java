package ru.clevertec.exception;

public class BankNotFoundException extends IllegalArgumentException {
    public BankNotFoundException(Long id) {
        super(String.format("Bank with id '%d' not found", id));
    }
}
