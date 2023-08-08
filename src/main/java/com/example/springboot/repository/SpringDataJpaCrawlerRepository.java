package com.example.springboot.repository;

import com.example.springboot.domain.Crawler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaCrawlerRepository extends JpaRepository<Crawler, Long>, CrawlerRepository {

    @Override
    Optional<Crawler> findByUrl(String url);

}
