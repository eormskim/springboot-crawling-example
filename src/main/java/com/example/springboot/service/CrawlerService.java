package com.example.springboot.service;

import com.example.springboot.domain.Crawler;
import com.example.springboot.repository.CrawlerRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CrawlerService {

    private final CrawlerRepository crawlerRepository;

    @Autowired
    public CrawlerService(CrawlerRepository crawlerRepository){
        this.crawlerRepository = crawlerRepository;
    }

    /**
     * 크롤링 데이터 가져오기 및 저장
     * @param
     * @return
     */
    public List<Crawler> getData(){
        String[] url = {"https://entertain.naver.com/now"};
        Stream<String> urlStream = Arrays.stream(url);
        List<Crawler> crawlerList = new ArrayList<>();

        urlStream.forEach(urlStr ->{
            try {
                List<Crawler> crawler = new ArrayList<>();
                Document doc = Jsoup.connect(urlStr).get();

                //경로 추가
                AtomicInteger idx = new AtomicInteger(0);
                System.out.println("Links on the page:");
                doc.select(".news_lst a[href]").stream()
                        .map(link -> link.attr("href"))
                        .forEach(s -> {
                            Crawler crawler1 = new Crawler();
                            crawler1.setUrl(s);
                            crawler.add(idx.getAndIncrement(),crawler1);
                        });

                //이미지 추가
                idx.set(0);
                System.out.println("Images on the page:");
                doc.select(".news_lst img[src]").stream()
                        .map(image -> image.attr("src"))
                        .forEach(s -> {
                            crawler.get(idx.getAndIncrement()).setImg(s);
                        });

                //타이틀 추가
                idx.set(0);
                System.out.println("Title on the page:");
                doc.select(".news_lst .tit_area > a[href]").stream()
                        .map(image -> image.childNode(0).toString())
                        .forEach(s ->{
                            crawler.get(idx.getAndIncrement()).setTitle(s);
                        });

                //중복 데이터 검증후 리스트에 추가
                crawlerList.addAll(validateDuplicateCrawlerData(crawler));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //TODO:: DB에 데이터 저장

        return crawlerList;
    }

    private List<Crawler> validateDuplicateCrawlerData(List<Crawler> crawlerList) {
        //중복 url 데이터 제거
        IntStream.range(0, crawlerList.size()).forEach(idx ->{
            crawlerRepository.findByUrl(crawlerList.get(idx).getUrl())
                    .ifPresent(m ->{
                        crawlerList.remove(idx);
                    });
        });
        return crawlerList;
    }

    /**
     * 전체 회원 조회
     * @return
     */
    /*public List<Crawler> findMembers(){
        return crawlerRepository.findAll();
    }

    public Optional<Crawler> findOne(Long memberId){
        return crawlerRepository.findById(memberId);
    }*/

}
