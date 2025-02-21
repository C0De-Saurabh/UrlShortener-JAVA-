package com.Saurabh.UrlShortener.ShortenedUrl;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "shortened_url")
@Data
public class ShortenedUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "originalUrl", nullable = false, length = 2048) // 2048 is the max length of a URL
    private String originalUrl;

    @Column(name = "short_code", nullable = false, length = 10, unique = true) // 10 is the length of the shortened URL
    private String shortCode;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false) // Automatically set on creation
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @UpdateTimestamp
    @Column(name = "updated_at") // Automatically updated on every change
    private LocalDateTime updatedAt;
}