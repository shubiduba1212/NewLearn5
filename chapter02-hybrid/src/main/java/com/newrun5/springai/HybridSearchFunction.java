package com.newrun5.springai;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
/*
Received message: 삼성전자 관련 뉴스를 검색하고 이를 바탕으로 2025년 전망을 알려주세요
call Request : 삼성전자 2025 전망
call Request : Samsung Electronics 2025 forecast
*/
@Component
@Description("Document search function, if the user asked to find documents or the assistant needed some references to make answers, the assistant can call this function to search documents. YOU MUST GENERATE SEARCH SINGLE QUERY COMBINED KOREAN AND ENGLISH SEARCH KEYWORDS")
@Slf4j
public class HybridSearchFunction implements Function<HybridSearchFunction.Request
        , HybridSearchFunction.Response>
{
    @Autowired
    MongoSearchService service;

    public static class Request
    {
        public String searchQuery;

        // 기본 생성자
        public Request() {}

        public Request(String searchQuery)
        {
            this.searchQuery = searchQuery;
        }
    }

    public static class Response
    {
        public List<Map<String, Object>> listMap;

        public Response() {};

        public Response(List<Map<String, Object>> listMap)
        {

            this.listMap = listMap;
        }
    }

    @Override
    public Response apply(Request t)
    {
        log.info("call Request : " + t.searchQuery);
        List<Map<String, Object>> list = service.hybridSearch(t.searchQuery);
        return new Response(list);
    }
}
