package ru.megaeater42.exception;

public class NasaApiError403Exception extends BaseNasaApiErrorException {
    public NasaApiError403Exception(int status, String error, String message) {
        super(status, error, message);
    }
}
