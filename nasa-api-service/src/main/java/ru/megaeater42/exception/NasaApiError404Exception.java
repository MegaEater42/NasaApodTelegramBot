package ru.megaeater42.exception;

public class NasaApiError404Exception extends BaseNasaApiErrorException {
    public NasaApiError404Exception(int status, String error, String message) {
        super(status, error, message);
    }
}
