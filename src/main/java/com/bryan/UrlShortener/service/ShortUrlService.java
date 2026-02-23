package com.bryan.UrlShortener.service;

import com.bryan.UrlShortener.Utils.ShortCodeGenerator;
import com.bryan.UrlShortener.exception.ShortCodeGenerationException;
import com.bryan.UrlShortener.exception.ShortUrlNotFoundException;
import com.bryan.UrlShortener.model.ShortUrl;
import com.bryan.UrlShortener.model.dto.CreateRequestDto;
import com.bryan.UrlShortener.model.dto.ShortUrlResponseDto;
import com.bryan.UrlShortener.model.dto.ShortUrlStatsDto;
import com.bryan.UrlShortener.model.dto.UpdateShortUrlDto;
import com.bryan.UrlShortener.repository.ShortUrlRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
public class ShortUrlService {
    private final ShortUrlRepository repo;
    private final int LIMIT_TRIES = 4;


    public ShortUrlService(ShortUrlRepository repo) {
        this.repo = repo;
    }

    public ShortUrlResponseDto create(CreateRequestDto dto) {

        ShortUrl save = null;
        for (int i = 0; i < LIMIT_TRIES; i++) {
            String shortCode = ShortCodeGenerator.generate();
            ShortUrl newShortUrl = new ShortUrl(
                    dto.url(),
                    shortCode,
                    0L
            );
            try {
                save = repo.save(newShortUrl);
                break;// si no se lanzo una excepcion rompemos bucle
            } catch (DataIntegrityViolationException ex) {
                //reintentamos
            }
        }

        if (save == null) {
            throw new ShortCodeGenerationException(LIMIT_TRIES);
        }

        return ShortUrlResponseDto.fromEntity(save) ;
    };

    public ShortUrlResponseDto getShortUrlByShortCode(String shortCode){
        ShortUrl shortUrl = repo.findByShortCode(shortCode).orElseThrow( () ->
                new ShortUrlNotFoundException(shortCode)
        );

        return ShortUrlResponseDto.fromEntity(shortUrl);
    }

    public ShortUrlResponseDto updateUrlByShortCode(String shortCode, UpdateShortUrlDto dto) {
        ShortUrl shortUrl = repo.findByShortCode(shortCode).orElseThrow( () ->
                new ShortUrlNotFoundException(shortCode)
        );

        shortUrl.setUrl(dto.url());
        ShortUrl updatedShortUrl = repo.save(shortUrl);

        return ShortUrlResponseDto.fromEntity(shortUrl);
    }

    @Transactional
    public void deleteByShortCode(String shortCode) {
        int deleted = repo.deleteByShortCode(shortCode);

        if (deleted == 0) {
            throw new ShortUrlNotFoundException(shortCode);
        }
    }

    public ShortUrlStatsDto getStatsByShortCode(String shortCode){
        ShortUrl shortUrl = repo.findByShortCode(shortCode).orElseThrow( () ->
                new ShortUrlNotFoundException(shortCode)
        );

        return ShortUrlStatsDto.fromEntity(shortUrl);
    }

    @Transactional
    public String getOriginalUrlAndIncrement(String shortCode) {
        ShortUrl shortUrl = repo.findByShortCode(shortCode)
                .orElseThrow(() -> new ShortUrlNotFoundException(shortCode));
        repo.incrementAccessCount(shortCode);
        return shortUrl.getUrl();
    }

    public Page<ShortUrl> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findAll(pageable);
    }
}
