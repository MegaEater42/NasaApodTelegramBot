package ru.megaeater42.proxy;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.megaeater42.dto.ApodDTO;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "nasa-api-service", configuration = NasaApiServiceProxy.NasaApiServiceProxyConfiguration.class)
public interface NasaApiServiceProxy {
    @GetMapping("/v1/apod")
    ApodDTO.Response.Inner getAPOD(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("apiKey") String apiKey
    );

    @GetMapping("/v1/apods-range")
    List<ApodDTO.Response.Inner> getAPODs(
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("apiKey") String apiKey
    );

    @GetMapping("/v1/apods-random")
    List<ApodDTO.Response.Inner> getAPODs(
            @RequestParam("count") Integer count,
            @RequestParam("apiKey") String apiKey
    );

    class NasaApiServiceProxyConfiguration {
        @Bean
        public BasicAuthRequestInterceptor basicAuthRequestInterceptor(@Value("${services.nasa-api-service.username}") String username, @Value("${services.nasa-api-service.password}") String password) {
            return new BasicAuthRequestInterceptor(username, password);
        }
    }
}