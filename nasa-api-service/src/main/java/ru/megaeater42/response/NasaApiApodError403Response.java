package ru.megaeater42.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NasaApiApodError403Response extends BaseNasaApiErrorResponse {
    private String message;
}