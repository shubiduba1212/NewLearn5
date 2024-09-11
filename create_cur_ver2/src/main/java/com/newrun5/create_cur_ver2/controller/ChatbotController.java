package com.newrun5.create_cur_ver2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.newrun5.create_cur_ver2.service.FinancialDataScraperService;

@Controller
public class ChatbotController {

    @Autowired
    private FinancialDataScraperService scraperService;

    // /search로 요청이 오면 search.html을 반환합니다.
    @GetMapping("/search")
    public String search() {
        return "search.html"; // search.html 파일의 경로
    }

    // POST 요청으로 재테크 관련 데이터를 스크래핑합니다.
    @PostMapping("/generate-curriculum")
    public ResponseEntity<String> generateCurriculum(@RequestBody QueryRequest request) {
        String query = request.getQuery();

        // 검색어를 이용하여 스크래핑 수행
        scraperService.scrapeAndStoreData(query);

        // 결과를 클라이언트에 반환
        return ResponseEntity.ok("Data submitted successfully.");
    }

    // QueryRequest 클래스 정의
    public static class QueryRequest {
        private String query;

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }
    }
}
