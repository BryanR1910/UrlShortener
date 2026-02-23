package com.bryan.UrlShortener.exception;

public class ShortUrlNotFoundException extends RuntimeException {
    public ShortUrlNotFoundException(String id) {
        super("Short url with short code: '%s' not found".formatted(id));
    }
}
