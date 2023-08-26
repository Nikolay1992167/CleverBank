package ru.clevertec.exception;

public class ResourceSqlException extends IllegalStateException {
    public ResourceSqlException() {
        super("Data base is not available");
    }

    public ResourceSqlException(String message) {
        super(message);
    }
}
