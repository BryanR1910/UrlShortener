package com.bryan.UrlShortener.controller;

import com.bryan.UrlShortener.service.ShortUrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;

@Controller
public class RedirectController {
    private final ShortUrlService shortUrlService;

    public RedirectController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable("shortCode") String shortCode){
        String origalUrl = shortUrlService.getOriginalUrlAndIncrement(shortCode);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(origalUrl)).build();
    }

}
