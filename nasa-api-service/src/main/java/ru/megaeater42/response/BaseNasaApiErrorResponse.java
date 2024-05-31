package ru.megaeater42.response;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseNasaApiErrorResponse {
    String code;
    @JsonSetter("service_version")
    private String serviceVersion;
}
