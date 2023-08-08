package com.example.springboot;

import com.example.springboot.aop.TimeTraceAop;
import com.example.springboot.repository.CrawlerRepository;
import com.example.springboot.repository.MemberRepository;
import com.example.springboot.service.CrawlerService;
import com.example.springboot.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final MemberRepository memberRepository;
    private final CrawlerRepository crawlerRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository, CrawlerRepository crawlerRepository) {
        this.memberRepository = memberRepository;
        this.crawlerRepository = crawlerRepository;
    }

    /*private EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em){
        this.em = em;
    }*/

    /*private DataSource dataSource;

    @Autowired
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }*/

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository);
    }
    @Bean
    public CrawlerService crawlerService(){
        return new CrawlerService(crawlerRepository);
    }

    @Bean
    public TimeTraceAop timeTraceAop() {
        return new TimeTraceAop();
    }

    //@Bean
    //public MemberRepository memberRepository(){
        //return new MemoryMemberRepository(dataSource);
        //return new JdbcMemberRepository(dataSource);
        //return new JdbcTemplateMemberRepository(dataSource);
        //return new JpaMemberRepository(memberRepository);
    //}
}
