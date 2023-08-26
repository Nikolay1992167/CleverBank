package ru.clevertec.exception;

public class AccountNotFoundException extends IllegalArgumentException {
    public AccountNotFoundException(Long id) {
        super(String.format("Account with id '%d' not found", id));
    }
}
