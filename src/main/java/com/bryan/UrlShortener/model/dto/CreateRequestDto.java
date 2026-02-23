package com.bryan.UrlShortener.model.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record CreateRequestDto(
        @NotBlank
        @URL
        String url
) {
}
