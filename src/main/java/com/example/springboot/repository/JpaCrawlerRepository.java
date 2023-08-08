package com.example.springboot.repository;

import com.example.springboot.domain.Crawler;
import com.example.springboot.domain.Member;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Transactional
public class JpaCrawlerRepository implements CrawlerRepository{

    private final EntityManager em;

    public JpaCrawlerRepository(EntityManager em) {
        this.em = em;
    }

    /*@Override
    public List<Crawler> save(Crawler crawler) {
        //em.persist(crawler);

        return null;
    }*/


    @Override
    public Optional<Crawler> findByUrl(String url) {
        List<Crawler> result = em.createQuery("select d from Crawler d where d.url = :url",Crawler.class)
                .setParameter("url", url)
                .getResultList();
        return result.stream().findAny();
    }
}
