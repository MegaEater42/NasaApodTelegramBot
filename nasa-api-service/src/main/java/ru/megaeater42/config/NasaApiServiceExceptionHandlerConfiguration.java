package ru.megaeater42.config;

import feign.FeignException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.megaeater42.exception.NasaApiError400Exception;
import ru.megaeater42.exception.NasaApiError403Exception;
import ru.megaeater42.exception.NasaApiError404Exception;
import ru.megaeater42.exception.NasaApiError429Exception;
import ru.megaeater42.response.BaseNasaApiServiceErrorResponse;

import java.time.LocalDateTime;

@RestControllerAdvice
@ApiResponse(content = @Content(schema = @Schema(implementation = BaseNasaApiServiceErrorResponse.class)))
public class NasaApiServiceExceptionHandlerConfiguration extends ResponseEntityExceptionHandler {
    @Override
    public ResponseEntity<Object> handleNoResourceFoundException(@Nullable NoResourceFoundException exception, @Nullable HttpHeaders headers, @Nullable HttpStatusCode status, @Nullable WebRequest request) {
        return new ResponseEntity<>(new BaseNasaApiServiceErrorResponse(LocalDateTime.now(), HttpStatus.valueOf(404).getReasonPhrase(), "Stolen pages...?"), HttpStatus.valueOf(404));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleBaseServiceException(NasaApiError400Exception exception) {
        return new ResponseEntity<>(new BaseNasaApiServiceErrorResponse(LocalDateTime.now(), exception.getError(), exception.getMessage()), HttpStatus.valueOf(exception.getStatus()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleBaseServiceException(NasaApiError403Exception exception) {
        return new ResponseEntity<>(new BaseNasaApiServiceErrorResponse(LocalDateTime.now(), exception.getError(), exception.getMessage()), HttpStatus.valueOf(exception.getStatus()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleBaseServiceException(NasaApiError404Exception exception) {
        return new ResponseEntity<>(new BaseNasaApiServiceErrorResponse(LocalDateTime.now(), exception.getError(), exception.getMessage()), HttpStatus.valueOf(exception.getStatus()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResponseEntity<?> handleBaseServiceException(NasaApiError429Exception exception) {
        return new ResponseEntity<>(new BaseNasaApiServiceErrorResponse(LocalDateTime.now(), exception.getError(), exception.getMessage()), HttpStatus.valueOf(exception.getStatus()));
    }


    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleFeignException(FeignException exception) {
        return new ResponseEntity<>(new BaseNasaApiServiceErrorResponse(LocalDateTime.now(), HttpStatus.valueOf(exception.status()).getReasonPhrase(), exception.getMessage()), HttpStatus.valueOf(exception.status()));
    }
}
