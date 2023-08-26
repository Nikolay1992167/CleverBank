package ru.clevertec.exception;

public class TransactionNotFoundException extends IllegalArgumentException{
    public TransactionNotFoundException(Long id) {
        super(String.format("Transaction with id '%d' not found", id));
    }
}
