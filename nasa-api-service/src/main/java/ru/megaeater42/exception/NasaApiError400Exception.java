package ru.megaeater42.exception;

public class NasaApiError400Exception extends BaseNasaApiErrorException {
    public NasaApiError400Exception(int status, String error, String message) {
        super(status, error, message);
    }
}
