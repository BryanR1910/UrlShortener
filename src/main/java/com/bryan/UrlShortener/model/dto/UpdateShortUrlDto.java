package com.bryan.UrlShortener.model.dto;


import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record UpdateShortUrlDto(
        @NotBlank()
        @URL
        String url
        ) {
}
