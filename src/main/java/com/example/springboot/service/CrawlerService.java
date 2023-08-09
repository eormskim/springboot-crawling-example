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
    public ArrayList crawling(){
        String[] url = {"https://entertain.naver.com/now"};
        Stream<String> urlStream = Arrays.stream(url);
        ArrayList crawlerArr = new ArrayList<>();

        urlStream.forEach(urlStr ->{
            try {
                List<Crawler> crawlerList = new ArrayList<>();
                Document doc = Jsoup.connect(urlStr).get();

                //경로 추가
                AtomicInteger idx = new AtomicInteger(0);
                doc.select(".news_lst2 a[href]").stream()
                        .map(link -> link.attr("href"))
                        .forEach(s -> {
                            Crawler crawler1 = new Crawler();
                            crawler1.setUrl(s);
                            crawlerList.add(idx.getAndIncrement(),crawler1);
                        });

                //이미지 추가
                idx.set(0);
                doc.select(".news_lst2 img[src]").stream()
                        .map(image -> image.attr("src"))
                        .forEach(s -> {
                            crawlerList.get(idx.getAndIncrement()).setImg(s);
                        });

                //타이틀 추가
                idx.set(0);
                doc.select(".news_lst2 .tit_area > a[href]").stream()
                        .map(image -> image.childNode(0).toString())
                        .forEach(s ->{
                            crawlerList.get(idx.getAndIncrement()).setTitle(s);
                        });

                //중복 데이터 검증후 리스트에 추가
                List<Crawler> refineCrawlerList = validateDuplicateCrawlerData(crawlerList);
                crawlerArr.addAll(refineCrawlerList);
                refineCrawlerList.forEach(crawlerRepository::save);


            } catch (IOException e) {
                e.printStackTrace();
            }
        });



        return crawlerArr;
    }

    private List<Crawler> validateDuplicateCrawlerData(List<Crawler> crawlerList) {
        //URL 중복인 경우 타이틀 NULL 처리
        IntStream.range(0, crawlerList.size()).forEach(idx -> {
            crawlerRepository.findByUrl(crawlerList.get(idx).getUrl())
                    .ifPresent(m ->crawlerList.get(idx).setTitle(null));
        });
        //TITLE이 NULL인 목록 제거
        IntStream.range(0, crawlerList.size())
                .filter(i -> crawlerList.get(i).getTitle() == null)
                .boxed()
                .sorted((i1, i2) -> Integer.compare(i2, i1))
                .forEach(index -> crawlerList.remove((int) index));

        return crawlerList;
    }

    /**
     * 전체 데이터 조회
     * @return
     */
    public List<Crawler> findCrawlingData(){
        return crawlerRepository.findAll();
    }
    /*

    public Optional<Crawler> findOne(Long memberId){
        return crawlerRepository.findById(memberId);
    }*/

}
