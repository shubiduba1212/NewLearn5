package com.newrun5.create_cur_ver2.service;

import com.newrun5.create_cur_ver2.model.DataItem;
import com.newrun5.create_cur_ver2.repository.DataItemRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class FinancialDataScraperService {

    // Google 검색 URL에 사용할 기본 URL
    private static final String GOOGLE_SEARCH_URL = "https://www.google.com/search?q=";

    @Autowired
    private DataItemRepository dataItemRepository; // DataItemRepository는 MongoDB에 데이터를 저장하는 인터페이스입니다.

    public void scrapeAndStoreData(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            String url = GOOGLE_SEARCH_URL + encodedQuery;

            Document doc = Jsoup.connect(url).get();

            // 원하는 데이터 추출
            // 예를 들어, 검색 결과에서 제목, 링크, 스니펫을 추출
            doc.select(".tF2Cxc").forEach(element -> {
                String title = element.select(".DKV0Md").text();
                String link = element.select(".yuRUbf a").attr("href");
                String snippet = element.select(".aCOpRe").text();

                DataItem dataItem = new DataItem();
                dataItem.setTitle(title);
                dataItem.setLink(link);
                dataItem.setSnippet(snippet);

                dataItemRepository.save(dataItem); // MongoDB에 저장
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
