package ru.megaeater42.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

public class ApodDTO {
    private interface ServiceVersion {
        String getServiceVersion();
    }
    private interface Copyright {
        String getCopyright();
    }
    private interface Date {
        @NotNull
        LocalDate getDate();
    }
    private interface Explanation {
        String getExplanation();
    }
    private interface HdUrl {
        String getHdUrl();
    }
    private interface MediaType {
        String getMediaType();
    }
    private interface Title {
        String getTitle();
    }
    private interface Url {
        String getUrl();
    }
    private interface ThumbnailUrl {
        String getThumbnailUrl();
    }

    public enum Response {;
        @Getter
        @Setter
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        @Schema(name = "ApodDTO.Response.Extern")
        public static class Extern implements ServiceVersion, Copyright, Date, Explanation, HdUrl, MediaType, Title, Url, ThumbnailUrl {
            // API stuff
            @JsonSetter("service_version")
            private String serviceVersion;

            // Content stuff
            private String copyright;
            private LocalDate date;
            private String explanation;
            @JsonSetter("hdurl")
            private String hdUrl;
            @JsonSetter("media_type")
            private String mediaType;
            private String title;
            private String url;
            @JsonSetter("thumbnail_url")
            private String thumbnailUrl;
        }

        @Getter
        @Setter
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        @Schema(name = "ApodDTO.Response.Inner")
        public static class Inner implements Copyright, Date, Explanation, HdUrl, MediaType, Title, Url, ThumbnailUrl {
            private String copyright;
            private LocalDate date;
            private String explanation;
            @JsonSetter("hdurl")
            private String hdUrl;
            @JsonSetter("media_type")
            private String mediaType;
            private String title;
            private String url;
            @JsonSetter("thumbnail_url")
            private String thumbnailUrl;
        }
    }
}