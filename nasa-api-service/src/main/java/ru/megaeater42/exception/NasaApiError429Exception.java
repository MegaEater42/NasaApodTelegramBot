package ru.megaeater42.exception;

public class NasaApiError429Exception extends BaseNasaApiErrorException {
    public NasaApiError429Exception(int status, String error, String message) {
        super(status, error, message);
    }
}
