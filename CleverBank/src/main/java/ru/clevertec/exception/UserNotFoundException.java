package ru.clevertec.exception;

public class UserNotFoundException extends IllegalArgumentException {
    public UserNotFoundException(Long id) {
        super(String.format("User with id '%d' not found", id));
    }
}
