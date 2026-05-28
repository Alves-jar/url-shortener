package com.noxus.urlShortener.repository;

import com.noxus.urlShortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Url, Long> {
}
