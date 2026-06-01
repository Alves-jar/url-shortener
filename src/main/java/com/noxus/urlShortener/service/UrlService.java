package com.noxus.urlShortener.service;

import com.noxus.urlShortener.model.Url;
import com.noxus.urlShortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class UrlService {

    private final UrlRepository repository;

    public UrlService(UrlRepository repository) {
        this.repository = repository;
    }

    public Url save(String url) {

        if (!verifyUrl(url)) {
            throw new IllegalArgumentException("Invalid URL");
        }

        try {

            URI uri = new URI(url);
            String path = uri.getPath();

            if (path == null || path.isEmpty()) {
                path = url;
            }

            String text = path.substring(0, Math.min(5, path.length()));
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(url.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();

            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            String shortCode = sb.substring(0, 8);
            Url shortUrl = Url.createUrl(url, shortCode);

            return repository.save(shortUrl);

        } catch (Exception e) {
            throw new RuntimeException("Error while saving URL", e);
        }
    }

    private boolean verifyUrl(String url) {

        try {
            new URI(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}