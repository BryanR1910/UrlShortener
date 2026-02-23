package com.bryan.UrlShortener.controller;

import com.bryan.UrlShortener.model.ShortUrl;
import com.bryan.UrlShortener.model.dto.CreateRequestDto;
import com.bryan.UrlShortener.model.dto.ShortUrlResponseDto;
import com.bryan.UrlShortener.model.dto.ShortUrlStatsDto;
import com.bryan.UrlShortener.model.dto.UpdateShortUrlDto;
import com.bryan.UrlShortener.service.ShortUrlService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/short-urls")
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @PostMapping()
    public ResponseEntity<ShortUrlResponseDto> createShortUrl(@Valid  @RequestBody CreateRequestDto dto){
        ShortUrlResponseDto response = shortUrlService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<Page<ShortUrl>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(shortUrlService.getAll(page, size));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<ShortUrlResponseDto> getShortUrlByShortCode(@PathVariable("shortCode") String shortCode){
        ShortUrlResponseDto response = shortUrlService.getShortUrlByShortCode(shortCode);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<ShortUrlStatsDto> getStastByShortCode(@PathVariable("shortCode") String shortCode){
        ShortUrlStatsDto response = shortUrlService.getStatsByShortCode(shortCode);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{shortCode}")
    public ResponseEntity<ShortUrlResponseDto> updateUrlByShortCode(@PathVariable("shortCode") String shortCode,@Valid @RequestBody UpdateShortUrlDto dto){
        ShortUrlResponseDto response = shortUrlService.updateUrlByShortCode(shortCode, dto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteShortUrlByShortCode(@PathVariable("shortCode") String shortCode){
        shortUrlService.deleteByShortCode(shortCode);

        return ResponseEntity.noContent().build();
    }
}
