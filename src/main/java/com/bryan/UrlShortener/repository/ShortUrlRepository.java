package com.bryan.UrlShortener.repository;

import com.bryan.UrlShortener.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    @Modifying
    @Query(value = """
                UPDATE ShortUrl
                SET accessCount = accessCount + 1
                WHERE shortCode = ?1
            """)
    int incrementAccessCount(String shortCode);

    Optional<ShortUrl> findByShortCode (String shortCode);

    int deleteByShortCode(String shortCode);
}