package com.bryan.UrlShortener.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String url;
    @Column(unique = true)
    String shortCode;
    Long accessCount;
    Instant createdAt;
    Instant updatedAt;

    @PrePersist
    private void beforePersist(){
        Instant instantDate = Instant.now();
        setCreatedAt(instantDate);
    }

    @PreUpdate
    private void beforeUpdate(){
        Instant instantDate = Instant.now();
        setUpdatedAt(instantDate);
    }

    public ShortUrl(){}

    public ShortUrl(String url, String shortCode, Long accessCount) {
        this.url = url;
        this.shortCode = shortCode;
        this.accessCount = accessCount;
    }

    public ShortUrl(Long id, String url, String shortCode, Instant createdAt, Instant updatedAt, Long accessCount) {
        this.id = id;
        this.url = url;
        this.shortCode = shortCode;
        this.accessCount = accessCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public Long getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(Long accessCount) {
        this.accessCount = accessCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }


}
