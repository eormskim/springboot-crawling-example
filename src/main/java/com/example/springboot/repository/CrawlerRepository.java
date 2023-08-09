package com.example.springboot.repository;

import com.example.springboot.domain.Crawler;

import java.util.List;
import java.util.Optional;

public interface CrawlerRepository {
    Crawler save(Crawler crawler);
    Optional<Crawler> findByUrl(String url);
    List<Crawler> findAll();
}
