package com.Saurabh.UrlShortener.controller;


import com.Saurabh.UrlShortener.Service.UrlShortenerService;
import com.Saurabh.UrlShortener.repository.UrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT} , allowedHeaders = "*")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @Autowired
    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    } // This is a constructor that initializes the UrlShortenerService

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody UrlRequest request) {
        if (request.getOriginalUrl() == null || request.getOriginalUrl().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Original URL cannot be null or empty");
        }

        String shortCode = urlShortenerService.shortenUrl(request.getOriginalUrl());
        return ResponseEntity.ok(shortCode);
    }


    @CrossOrigin(origins = "*") // Allow requests from any frontend
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginal(@PathVariable String shortCode) {
        String originalUrl = urlShortenerService.getOriginalUrl(shortCode);

        if (originalUrl == null || originalUrl.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }


}
