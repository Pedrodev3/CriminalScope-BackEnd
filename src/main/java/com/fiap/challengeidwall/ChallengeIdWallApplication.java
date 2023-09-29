package com.fiap.challengeidwall;

import com.fiap.challengeidwall.services.FBIWebScrapingService;
import com.fiap.challengeidwall.services.InterpolWebScrapingService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;

@SpringBootApplication
//@EnableSwagger2
public class ChallengeIdWallApplication {

    @Autowired
    private FBIWebScrapingService fbiWebScrapingService;

    @Autowired
    private InterpolWebScrapingService interpolWebScrapingService;

    public static void main(String[] args) {
        SpringApplication.run(ChallengeIdWallApplication.class, args);
    }

//    @PostConstruct
//    public void initFbi() {
//        try {
//            fbiWebScrapingService.scrapeAndInsertData();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @PostConstruct
    public void initInterpol() {
        try {
            interpolWebScrapingService.scrapeAndInsertData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
