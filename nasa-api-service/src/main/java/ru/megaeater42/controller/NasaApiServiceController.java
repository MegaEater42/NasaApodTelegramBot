package ru.megaeater42.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.megaeater42.dto.ApodDTO;
import ru.megaeater42.proxy.NasaApiProxy;
import ru.megaeater42.response.BaseNasaApiServiceErrorResponse;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/", produces = MediaType.APPLICATION_JSON_VALUE)
@OpenAPIDefinition(info = @Info(title = "NasaApiService API", version = "v1"))
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = BaseNasaApiServiceErrorResponse.class))),
        @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = BaseNasaApiServiceErrorResponse.class))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = BaseNasaApiServiceErrorResponse.class))),
        @ApiResponse(responseCode = "429", content = @Content(schema = @Schema(implementation = BaseNasaApiServiceErrorResponse.class))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = BaseNasaApiServiceErrorResponse.class)))
})
public class NasaApiServiceController {
    private final NasaApiProxy nasaApiProxy;

    @GetMapping(value ="/apod")
    public ApodDTO.Response.Inner getAPOD(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("apiKey") String apiKey
    ) {
        return nasaApiProxy.getAPOD(date, true, apiKey);
    }

    @GetMapping("/apods-range")
    public List<ApodDTO.Response.Inner> getAPODs(
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("apiKey") String apiKey
    ) {
        return nasaApiProxy.getAPODs(startDate, endDate, null, true, apiKey);
    }

    @GetMapping("/apods-random")
    public List<ApodDTO.Response.Inner> getAPODs(
            @RequestParam("count") Integer count,
            @RequestParam("apiKey") String apiKey
    ) {
        return nasaApiProxy.getAPODs( null, null, count, true, apiKey);
    }
}


