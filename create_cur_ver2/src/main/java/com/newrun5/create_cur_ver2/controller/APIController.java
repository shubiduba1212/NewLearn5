package com.newrun5.create_cur_ver2.controller;

//import com.newrun5.create_cur_ver2.service.ChatbotService;
import com.newrun5.create_cur_ver2.service.FinancialDataScraperService;
import com.newrun5.create_cur_ver2.service.OpenAIChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired
    private FinancialDataScraperService scraperService;

    @Autowired
    private OpenAIChatbotService chatbotService;


    @PostMapping("/scrape")
    public String scrapeData(@RequestParam String query) {
        scraperService.scrapeAndStoreData(query);
        return "{\"message\": \"Data scraped and stored successfully\"}";
    }

//    @Autowired
//    private ChatbotService chatbotService; // ChatbotService를 사용하여 커리큘럼을 생성합니다.

//    @PostMapping("/generate-curriculum")
//    public String generateCurriculum(@RequestParam String query) {
//        // 사용자가 입력한 검색어를 기반으로 커리큘럼을 생성합니다.
//        return chatbotService.generateCurriculum();
//    }

}



//@PostMapping("/generate-curriculum")
//public ResponseEntity<String> generateCurriculum(@RequestBody String userInput) {
//    // Step 1: 데이터 스크래핑 및 저장
//    scraperService.scrapeAndStoreData(userInput);
//
//    // Step 2: 챗봇을 통해 커리큘럼 생성
//    String response = chatbotService.generateCurriculum(userInput);
//
//    return ResponseEntity.ok(response);
//}

