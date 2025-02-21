package com.Saurabh.UrlShortener.Service;

import com.Saurabh.UrlShortener.ShortenedUrl.ShortenedUrl;
import com.Saurabh.UrlShortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UrlShortenerService {

    @Autowired
    private UrlRepository urlRepository;

    private static final String REDIS_KEY_PREFIX = "URL:";
    private static final int MAX_RETRIES = 5; // To prevent infinite loops

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * Shortens the given URL and stores it in the database and Redis cache.
     */
    public String shortenUrl(String originalUrl) {
        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            originalUrl = "https://" + originalUrl;  // Default to HTTPS if missing
        }

        String shortCode = generateShortCode(originalUrl);
        int attempts = 0;

        while (urlRepository.existsByShortCode(shortCode)) {
            if (attempts >= MAX_RETRIES) {
                throw new RuntimeException("Failed to generate a unique short code after " + MAX_RETRIES + " attempts.");
            }
            shortCode = generateShortCode(originalUrl + UUID.randomUUID());
            attempts++;
        }

        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setOriginalUrl(originalUrl);
        shortenedUrl.setShortCode(shortCode);
        urlRepository.save(shortenedUrl);

        redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + shortCode, originalUrl, 1, TimeUnit.HOURS);

        return shortCode;
    }


    //Fetches the original URL from the database or Redis cache using the short code
    public String getOriginalUrl(String shortCode) {
        // Check Redis cache first
        Object cachedUrl = redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + shortCode);
        if (cachedUrl != null) {
            return cachedUrl.toString();
        }

        // Fetch from DB if not in cache
        Optional<ShortenedUrl> shortenedUrl = urlRepository.findByShortCode(shortCode);
        if (shortenedUrl.isPresent()) {
            String originalUrl = shortenedUrl.get().getOriginalUrl();
            redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + shortCode, originalUrl, 1, TimeUnit.HOURS); // Cache it
            System.out.println("Original URL: " + originalUrl);
            return originalUrl;
        }

        throw new RuntimeException("Shortened URL Not Found");
    }

    //Generates a short code for the given URL
    private String generateShortCode(String url) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest((url + UUID.randomUUID()).getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash).substring(0, 8);
        } catch (Exception e) {
            throw new RuntimeException("Error generating short code", e);
        }
    }
}

