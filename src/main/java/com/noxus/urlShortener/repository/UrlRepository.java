package com.noxus.urlShortener.repository;

import com.noxus.urlShortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByOriginalUrl(String originalUrl);

}