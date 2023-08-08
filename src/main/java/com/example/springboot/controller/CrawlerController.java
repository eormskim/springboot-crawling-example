package com.example.springboot.controller;

import com.example.springboot.domain.Crawler;
import com.example.springboot.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String crawlerFrom(Model model){
        List<Crawler> crawler = new ArrayList<>();
        model.addAttribute("crawler", crawler);
        return "crawler/crawlingList";
    }

    @PostMapping("/crawler/getCrawlerData")
    @ResponseBody
    public List<Crawler> getCrawlerData(){
        List<Crawler> crawlerList = crawlerService.getData();
        return crawlerList;
    }

}
