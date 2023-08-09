package com.example.springboot.controller;

import com.example.springboot.domain.Crawler;
import com.example.springboot.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CrawlerController {

    private final CrawlerService crawlerService;

    @Autowired
    public CrawlerController(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @GetMapping("/crawler")
    public String crawlerFrom(Model model
            ,@RequestParam(required = false, defaultValue = "0", value = "page") int page){

        //불러올 페이지의 데이터 1페이지는 0부터 시작
        Page<Crawler> listPage =  crawlerService.findCrawlingData(page);

        //총페이지수
        int totalPage = listPage.getTotalPages();

        model.addAttribute("crawler", listPage.getContent());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageNo", page);
        model.addAttribute("number", listPage.getNumber());
        model.addAttribute("resultDataTotal", listPage.getTotalElements());
        model.addAttribute("size", listPage.getSize());

        return "crawler/crawlingList";
    }

    @PostMapping("/crawler/getCrawlerData")
    @ResponseBody
    public ArrayList getCrawlerData(){
        ArrayList crawlerList = crawlerService.crawling();
        return crawlerList;
    }

}
