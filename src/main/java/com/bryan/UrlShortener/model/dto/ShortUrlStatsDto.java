package com.bryan.UrlShortener.model.dto;

import com.bryan.UrlShortener.model.ShortUrl;

import java.time.Instant;

public record ShortUrlStatsDto(
        Long id,
        String url,
        String shortCode,
        Instant createdAt,
        Instant updatedAt,
        Long accessCount
) {
    public static ShortUrlStatsDto fromEntity(ShortUrl shortUrl){
        return new ShortUrlStatsDto(
                shortUrl.getId(),
                shortUrl.getUrl(),
                shortUrl.getShortCode(),
                shortUrl.getCreatedAt(),
                shortUrl.getUpdatedAt(),
                shortUrl.getAccessCount()
        );
    }
}
