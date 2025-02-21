package com.Saurabh.UrlShortener.repository;



import com.Saurabh.UrlShortener.ShortenedUrl.ShortenedUrl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<ShortenedUrl, Long> {


    Optional<ShortenedUrl> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);

}