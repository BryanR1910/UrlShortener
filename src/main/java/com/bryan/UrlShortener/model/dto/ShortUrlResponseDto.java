package com.bryan.UrlShortener.model.dto;

import com.bryan.UrlShortener.model.ShortUrl;

import java.time.Instant;

public record ShortUrlResponseDto(
        Long id,
        String url,
        String shortCode,
        Instant createdAt,
        Instant updatedAt
) {

    public static ShortUrlResponseDto fromEntity(ShortUrl shortUrl){
        return new ShortUrlResponseDto(
                shortUrl.getId(),
                shortUrl.getUrl(),
                shortUrl.getShortCode(),
                shortUrl.getCreatedAt(),
                shortUrl.getUpdatedAt()
        );
    }
}
