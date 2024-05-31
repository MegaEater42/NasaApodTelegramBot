package ru.megaeater42.response;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NasaApiApodError400Response extends BaseNasaApiErrorResponse {
    @JsonSetter("msg")
    private String message;
}