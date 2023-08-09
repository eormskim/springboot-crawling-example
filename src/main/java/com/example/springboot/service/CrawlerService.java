package com.example.springboot.service;

import com.example.springboot.domain.Crawler;
import com.example.springboot.repository.CrawlerRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.jsoup.select.Elements;
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
        String entertainLink = "https://entertain.naver.com";
        String[] url = {entertainLink +"/now"};
        Stream<String> urlStream = Arrays.stream(url);
        ArrayList crawlerArr = new ArrayList<>();

        urlStream.forEach(urlStr ->{
            try {
                List<Crawler> crawlerList = new ArrayList<>();
                Document doc = Jsoup.connect(urlStr).get();

                Elements ele = doc.select(".news_lst2");
                for(Element element : ele){
                    Crawler crawler = new Crawler();
                    //이미지 추가
                    crawler.setImg(element.select("img[src]").attr("src"));
                    //경로 추가
                    crawler.setUrl(entertainLink + element.select(".tit_area > a[href]").attr("href"));
                    //타이틀 추가
                    crawler.setTitle(element.select(".tit_area > a[href]").get(0).childNode(0).toString());
                    crawlerList.add(crawler);
                }

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
                .forEach(integer -> {
                    crawlerList.remove((int)integer);
                });

        return crawlerList;
    }

    /**
     * 전체 데이터 조회
     * @return
     */
    public Page<Crawler> findCrawlingData(int page){
        return crawlerRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "regDate")));
    }
    /*

    public Optional<Crawler> findOne(Long memberId){
        return crawlerRepository.findById(memberId);
    }*/

}
