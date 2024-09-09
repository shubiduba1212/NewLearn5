package com.newrun5.springai;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class APIController
{
    @Autowired
    private MongoDBAtlasVectorStore mongodbVectorStore;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    EmbeddingModel embeddingModel;

    @RequestMapping("/insert")
    public Object insert(@RequestBody ArticleRequest request)
    {
        Document doc = new Document(request.getContent(),
                Map.of("author", request.getAuthor(), "type", "report", "date",
                        request.getCreatedAt(), "title", request.getTitle()));
        List<Document> list = new ArrayList<>();
        list.add(doc);
        mongodbVectorStore.add(list);
        return doc;
    }

    //Spring Boot의 REST 컨트롤러에서 벡터 검색을 수행하는 search 메서드를 구현한 것
    @RequestMapping("/search")
    public Object search(@RequestParam String query)
    {
//        float[] queryVector = embeddingModel.embed(query);
        float[] queryVector = embeddingModel.embed(query);
        List<Double> queryVectorList = new ArrayList<>();
        for (float value : queryVector) {
            queryVectorList.add((double) value);
        }
//        List<Double> queryVector = embeddingModel.embed(query);

        //org.bson.Document: MongoDB에서 사용되는 데이터 형식으로, MongoDB 문서(데이터)를 표현하는 클래스로,
        // BSON(Binary JSON) 형식의 데이터를 담을 수 있다.
        org.bson.Document vectorSearchStage = new org.bson.Document("$vectorSearch",
                new org.bson.Document("index", "vector_index") // 벡터 인덱스 이름
                        .append("path", "embedding")
                        .append("queryVector", queryVectorList) // 벡터로 변환된 쿼리
                        .append("numCandidates", 64) // 후보 문서 수
                        .append("limit", 10)); // 후보 문서 수

        //MongoDB의 Aggregation 파이프라인을 정의
        //많은 정보를 원하는 형태로 뽑아내기 위한 여러 단계의 작업을 자동으로 수행해주는 도구(필터링/정리/정렬)
        Aggregation aggregation = Aggregation.newAggregation(
                context -> vectorSearchStage, //벡터 검색 단계를 포함하는 파이프라인 단계
                // $project 단계 추가
                context -> new org.bson.Document("$project",
                        new org.bson.Document("metadata", 1)
                                .append("content", 1)
                                .append("media", 1)
                                .append("score", new org.bson.Document("$meta"
                                        , "vectorSearchScore")) // 검색 점수 추가
                ),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "score")), // 검색 점수를 기준으로 내림차순으로 정렬
                Aggregation.limit(10) // 상위 10개의 결과만 가져옴
        );

        // Aggregation 실행 및 결과 반환
        AggregationResults<org.bson.Document> results = mongoTemplate.aggregate(
                aggregation, "documents", org.bson.Document.class);
        return results.getMappedResults();
    }

    @RequestMapping("/text-search")
    public List<org.bson.Document> textSearch(String query)
    {
        //List<org.bson.Document> : 여러 개의 MongoDB 문서를 포함할 수 있는 리스트
        org.bson.Document searchStage = new org.bson.Document("$search",
                new org.bson.Document("index", "text_index") // 텍스트 인덱스 이름
                        .append("text",
                                new org.bson.Document("query", query) // 검색어가 포함된 변수
                                        .append("path", List.of("content", "metadata.title"
                                                , "metadata.author")) // 검색할 필드 목록
                        )
        );

        // MongoDB Aggregation 생성
        Aggregation aggregation = Aggregation.newAggregation(
                context -> searchStage,
                // $project 단계 추가
                context -> new org.bson.Document("$project",
                        new org.bson.Document("metadata", 1)
                                .append("content", 1)
                                .append("media", 1)
                                .append("score", new org.bson.Document("$meta", "searchScore"))
                        // 검색 점수 추가
                ),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "score")), // 점수에 따라 정렬
                Aggregation.limit(10) // 상위 10개의 결과만 가져옴
        );

        // Aggregation 실행 및 결과 반환
        AggregationResults<org.bson.Document> results = mongoTemplate.aggregate(
                aggregation, "documents", org.bson.Document.class);
        return results.getMappedResults();
    }

    @RequestMapping("/hybrid-search")
    public List<Map<String, Object>> hybridSearch(String query)
    {
        // 벡터 검색 쿼리 생성
//        float[] queryVector = embeddingModel.embed(query);
        float[] queryVector = embeddingModel.embed(query);
        List<Double> queryVectorList = new ArrayList<>();
        for (float value : queryVector) {
            queryVectorList.add((double) value);
        }

        org.bson.Document vectorSearchStage = new org.bson.Document("$vectorSearch",
                new org.bson.Document("index", "vector_index")
                        .append("path", "embedding")
                        .append("queryVector", queryVectorList)
                        .append("numCandidates", 64)
                        .append("limit", 10)
        );

        // 벡터 검색 Aggregation 생성 및 실행
        Aggregation aggregation1 = Aggregation.newAggregation(
                context -> vectorSearchStage,
                context -> new org.bson.Document("$project",
                        new org.bson.Document("metadata", 1)
                                .append("content", 1)
                                .append("media", 1)
                                .append("vectorSearchScore", new org.bson.Document("$meta"
                                        , "vectorSearchScore"))
                ),
                Aggregation.limit(10)
        );

        AggregationResults<org.bson.Document> result1 = mongoTemplate.aggregate(
                aggregation1, "documents", org.bson.Document.class);


        // 텍스트 검색 쿼리 생성
        org.bson.Document searchStage = new org.bson.Document("$search",
                new org.bson.Document("index", "vector_index")
                        .append("text",
                                new org.bson.Document("query", query)
                                        .append("path", List.of("content", "metadata.title"
                                                , "metadata.author"))
                        )
        );

        // 텍스트 검색 Aggregation 생성 및 실행
        Aggregation aggregation2 = Aggregation.newAggregation(
                context -> searchStage,
                context -> new org.bson.Document("$project",
                        new org.bson.Document("metadata", 1)
                                .append("content", 1)
                                .append("media", 1)
                                .append("searchScore", new org.bson.Document("$meta", "searchScore"))
                ),
                Aggregation.limit(10)
        );

        AggregationResults<org.bson.Document> result2 = mongoTemplate.aggregate(
                aggregation2, "documents", org.bson.Document.class);

        // 3. 결과를 List<Map<String, Object>>로 변환 및 중복 제거 후 점수 합산
        Map<String, Map<String, Object>> combinedResultsMap = new HashMap<>();

        // 벡터 검색 결과 처리
        for (org.bson.Document doc : result1.getMappedResults()) {
            String id = doc.getString("_id");
            double vectorScore = doc.getDouble("vectorSearchScore");

            Map<String, Object> resultMap = combinedResultsMap.getOrDefault(id
                    , new HashMap<>(doc));
            resultMap.put("vectorSearchScore", vectorScore);
            resultMap.put("score", vectorScore * 1.0); // 초기 score는 vectorScore로 설정
            combinedResultsMap.put(id, resultMap);
        }

        // 텍스트 검색 결과 처리
        for (org.bson.Document doc : result2.getMappedResults())
        {
            String id = doc.getString("_id");
            double textScore = doc.getDouble("searchScore");

            Map<String, Object> resultMap = combinedResultsMap.getOrDefault(id
                    , new HashMap<>(doc));
            double currentScore = (double) resultMap.getOrDefault("score", 0.0);
            resultMap.put("searchScore", textScore);
            resultMap.put("score", currentScore + textScore * 0.5);
            // 기존 score에 searchScore를 더함
            combinedResultsMap.put(id, resultMap);
        }

        // 결과를 리스트로 변환 및 정렬
        List<Map<String, Object>> combinedResults = new ArrayList<>(
                combinedResultsMap.values());
        combinedResults.sort((a, b) -> Double.compare((double) b.get("score")
                , (double) a.get("score")));

        return combinedResults;
    }
}
