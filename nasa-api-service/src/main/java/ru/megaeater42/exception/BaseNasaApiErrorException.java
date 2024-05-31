package ru.megaeater42.exception;

import lombok.Getter;

@Getter
public class BaseNasaApiErrorException extends RuntimeException {
    private final int status;
    private final String error;

    public BaseNasaApiErrorException(int status, String error, String message) {
        super(message);
        this.status = status;
        this.error = error;
    }
}
