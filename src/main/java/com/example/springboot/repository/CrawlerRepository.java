package com.example.springboot.repository;

import com.example.springboot.domain.Crawler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CrawlerRepository {
    Crawler save(Crawler crawler);
    Optional<Crawler> findByUrl(String url);
    Page<Crawler> findAll(Pageable pageable);
}
