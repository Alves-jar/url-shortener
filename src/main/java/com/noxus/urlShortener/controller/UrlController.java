package com.noxus.urlShortener.controller;

import com.noxus.urlShortener.dto.UrlRequest;
import com.noxus.urlShortener.model.Url;
import com.noxus.urlShortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/url")
public class UrlController {

    @Autowired
    UrlService service;

    @PostMapping("/save")
    public ResponseEntity<Url> save(@RequestBody UrlRequest request) {

        Url url = service.save(request.url());

        return ResponseEntity
            .created(URI.create("/url/" + url.getShortenCode()))
            .body(url);
    }
}
