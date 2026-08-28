package com.bryan.UrlShortener.model.dto;

import com.bryan.UrlShortener.model.ShortUrl;

public record ShortUrlStatsDto(Long id, String url, String shortCode, Long accessCount) {
    public static ShortUrlStatsDto fromEntity(ShortUrl shortUrl) {
        return new ShortUrlStatsDto(
                shortUrl.getId(), shortUrl.getUrl(), shortUrl.getShortCode(), shortUrl.getAccessCount());
    }
}
