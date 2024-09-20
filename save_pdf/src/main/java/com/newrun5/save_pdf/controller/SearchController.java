package com.newrun5.save_pdf.controller;

import com.newrun5.save_pdf.service.MongoSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "http://localhost:8080") // 클라이언트에서 접근 허용
public class SearchController {

    @Autowired
    MongoSearchService mongoSearchService;

    @PostMapping("/hybrid")
    public List<Map<String, Object>> hybridSearch(@RequestBody Map<String, String> body) {
        String query = body.get("query");
        return mongoSearchService.hybridSearch(query);
    }
}




