package com.bryan.UrlShortener.model.payload;

import java.time.Instant;

public class ApiResponse {
    private Instant time;
    private String url;
    private String message;

    public ApiResponse(String url, String message) {
        this.time = Instant.now();
        this.url = url.replace("uri=", "");
        this.message = message;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
