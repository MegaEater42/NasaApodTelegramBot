package ru.megaeater42.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.megaeater42.dto.ApodDTO;
import ru.megaeater42.exception.NasaApiError404Exception;
import ru.megaeater42.exception.NasaApiError429Exception;
import ru.megaeater42.response.NasaApiApodError400Response;
import ru.megaeater42.response.NasaApiApodError403Response;
import ru.megaeater42.exception.NasaApiError400Exception;
import ru.megaeater42.exception.NasaApiError403Exception;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface NasaApiApodProxy {
    @GetMapping("/apod")
    ApodDTO.Response.Inner getAPOD(
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name = "thumbs", required = false) Boolean thumbs,
            @RequestParam(name = "api_key", required = false) String apiKey
    );

    @GetMapping("/apod")
    List<ApodDTO.Response.Inner> getAPODs(
            @RequestParam(name = "start_date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "end_date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(name = "count", required = false) Integer count,
            @RequestParam(name = "thumbs", required = false) Boolean thumbs,
            @RequestParam(name = "api_key", required = false) String apiKey
    );

    @Configuration
    class NasaApiApodProxyConfiguration {
        @Bean
        public ErrorDecoder getErrorDecoder() {
            return new CustomErrorDecoder();
        }

        @Slf4j
        public static class CustomErrorDecoder implements ErrorDecoder {
            private final ErrorDecoder errorDecoder = new Default();

            @Override
            public Exception decode(String methodKey, Response response) {
                switch (response.status()) {
                    case 400, 404 -> {
                        NasaApiApodError400Response error;
                        try (InputStream bodyIs = response.body().asInputStream()) {
                            ObjectMapper mapper = new ObjectMapper();
                            error = mapper.readValue(bodyIs, NasaApiApodError400Response.class);
                        }
                        catch (IOException e) {
                            return new RuntimeException(e.getMessage());
                        }
                        if (response.status() == 400) {
                            return new NasaApiError400Exception(response.status(), HttpStatus.valueOf(response.status()).getReasonPhrase(), error.getMessage() != null ? error.getMessage() : HttpStatus.valueOf(response.status()).getReasonPhrase());
                        } else {
                            return new NasaApiError404Exception(response.status(), HttpStatus.valueOf(response.status()).getReasonPhrase(), error.getMessage() != null ? error.getMessage() : HttpStatus.valueOf(response.status()).getReasonPhrase());
                        }
                    }
                    case 403, 429 -> {
                        Map<String, NasaApiApodError403Response> error;
                        try (InputStream bodyIs = response.body().asInputStream()) {
                            ObjectMapper mapper = new ObjectMapper();
                            error = mapper.readValue(bodyIs, mapper.getTypeFactory().constructMapType(Map.class, String.class, NasaApiApodError403Response.class));
                        }
                        catch (IOException e) {
                            return new RuntimeException(e.getMessage());
                        }
                        if (response.status() == 403) {
                            return new NasaApiError403Exception(response.status(), HttpStatus.valueOf(response.status()).getReasonPhrase(), error.get("error").getMessage() != null ? error.get("error").getMessage() : "Forbidden");
                        } else {
                            return new NasaApiError429Exception(response.status(), HttpStatus.valueOf(response.status()).getReasonPhrase(), error.get("error").getMessage() != null ? error.get("error").getMessage() : "Too many requests");
                        }
                    }
                    default -> {
                        return errorDecoder.decode(methodKey, response);
                    }
                }
            }
        }
    }
}
