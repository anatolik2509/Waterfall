package ru.itis.antonov.waterfall.exceptions;

public class OccupiedLoginException extends RuntimeException{
    public OccupiedLoginException() {
    }

    public OccupiedLoginException(String message) {
        super(message);
    }

    public OccupiedLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public OccupiedLoginException(Throwable cause) {
        super(cause);
    }
}
