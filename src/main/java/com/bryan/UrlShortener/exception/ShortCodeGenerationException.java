package com.bryan.UrlShortener.exception;

public class ShortCodeGenerationException extends RuntimeException {
    public ShortCodeGenerationException(int limitTries) {
        super("Failed to generate a unique short code after " + limitTries + " attempts");
    }
}
