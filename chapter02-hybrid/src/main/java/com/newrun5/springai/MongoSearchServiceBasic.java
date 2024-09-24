package com.newrun5.springai;

import org.bson.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MongoSearchServiceBasic
{
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    EmbeddingModel embeddingModel;

    @Value("${spring.ai.vectorstore.mongodb.weight.vectorscore}")
    private double vectorScoreWeight;

    @Value("${spring.ai.vectorstore.mongodb.weight.textscore}")
    private double textScoreWeight;

    public List<Map<String, Object>> vectorSearch(String query) {
        float[] queryVector = embeddingModel.embed(query);
        List<Double> queryVectorList = new ArrayList<>();
        for (float value : queryVector) {
            queryVectorList.add((double) value);
        }

        org.bson.Document vectorSearchStage = new org.bson.Document("$vectorSearch",
                new org.bson.Document("index", "vector_index") // 벡터 인덱스 이름
                        .append("path", "embedding")
                        .append("queryVector", queryVectorList) // 벡터로 변환된 쿼리
                        .append("numCandidates", 64) // 후보 문서 수
                        .append("limit", 10)); // 후보 문서 수

        Aggregation aggregation = Aggregation.newAggregation(
                context -> vectorSearchStage,
                // $project 단계 추가
                context -> new org.bson.Document("$project",
                        new org.bson.Document("metadata", 1)
                                .append("content", 1)
                                .append("media", 1)
                                .append("vectorSearchScore", new org.bson.Document("$meta", "vectorSearchScore"))
                                .append("score", new org.bson.Document("$meta", "vectorSearchScore"))
                ),
                // score 필터링
                context -> new org.bson.Document("$match",
                        new org.bson.Document("score", new org.bson.Document("$gte", 0.9))
                ),
                // 점수에 따라 정렬
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "score")),
                // 상위 10개의 결과만 가져옴
                Aggregation.limit(10)
        );

        // Aggregation 실행
        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "documents", Document.class);

        // Document를 List<Map<String, Object>>로 변환
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Document document : results.getMappedResults()) {
            resultList.add(document);
        }

        return resultList;
    }
//    public List<Map<String, Object>> vectorSearch(String query) {
//        AggregationResults<Document> results = vectorSearch(query);
//        List<Map<String, Object>> documents = new ArrayList<>();
//        for (Document doc : results.getMappedResults()) {
//            documents.add(doc);
//        }
//        return documents;
//    }
//    public AggregationResults<Document> vectorSearch(String query)
//    {
////        float[] queryVector = embeddingModel.embed(query);
//        float[] queryVector = embeddingModel.embed(query);
//        List<Double> queryVectorList = new ArrayList<>();
//        for (float value : queryVector) {
//            queryVectorList.add((double) value);
//        }
//
//        org.bson.Document vectorSearchStage = new org.bson.Document("$vectorSearch",
//                new org.bson.Document("index", "vector_index") // 벡터 인덱스 이름
//                        .append("path", "embedding")
//                        .append("queryVector", queryVectorList) // 벡터로 변환된 쿼리
//                        .append("numCandidates", 64) // 후보 문서 수
//                        .append("limit", 10)); // 후보 문서 수
//
//        Aggregation aggregation = Aggregation.newAggregation(
//                context -> vectorSearchStage,
//                // $project 단계 추가
//                context -> new org.bson.Document("$project",
//                        new org.bson.Document("metadata", 1)
//                                .append("content", 1)
//                                .append("media", 1)
//                                .append("vectorSearchScore", new org.bson.Document("$meta"
//                                        , "vectorSearchScore")) // 검색 점수 추가
//                                .append("score", new org.bson.Document("$meta"
//                                        , "vectorSearchScore")) // 검색 점수 추가
//                ),
//                Aggregation.sort(Sort.by(Sort.Direction.DESC, "score")), // 점수에 따라 정렬
//                Aggregation.limit(10) // 상위 10개의 결과만 가져옴
//        );
//
//        // Aggregation 실행 및 결과 반환
//        return mongoTemplate.aggregate(aggregation, "documents"
//                , org.bson.Document.class);
//    }

    public AggregationResults<Document> textSearch(String query)
    {
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
                                .append("searchScore", new org.bson.Document("$meta", "searchScore"))
                                // 검색 점수 추가
                                .append("score", new org.bson.Document("$meta", "searchScore"))
                        // 검색 점수 추가
                ),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "score")), // 점수에 따라 정렬
                Aggregation.limit(10) // 상위 10개의 결과만 가져옴
        );

        // Aggregation 실행 및 결과 반환
        return mongoTemplate.aggregate(aggregation, "documents"
                , org.bson.Document.class);
    }

//    public List<Map<String, Object>> hybridSearch(String query)
//    {
//        AggregationResults<Document> result1 =  vectorSearch(query);
//        AggregationResults<Document> result2 =  textSearch(query);
//
//        Map<String, Map<String, Object>> combinedResultsMap = new HashMap<>();
//
//        // 벡터 검색 결과 처리
//        for (org.bson.Document doc : result1.getMappedResults())
//        {
//            String id = doc.getString("_id");
//            double vectorScore = doc.getDouble("vectorSearchScore");
//
//            Map<String, Object> resultMap = combinedResultsMap.getOrDefault(id
//                    , new HashMap<>(doc));
//            resultMap.put("vectorSearchScore", vectorScore);
//            resultMap.put("score", vectorScore * 1.0); // 초기 score는 vectorScore로 설정
//            combinedResultsMap.put(id, resultMap);
//        }
//
//        // 텍스트 검색 결과 처리
//        for (org.bson.Document doc : result2.getMappedResults())
//        {
//            String id = doc.getString("_id");
//            double textScore = doc.getDouble("searchScore");
//
//            Map<String, Object> resultMap = combinedResultsMap.getOrDefault(id
//                    , new HashMap<>(doc));
//            double currentScore = (double) resultMap.getOrDefault("score", 0.0);
//            resultMap.put("searchScore", textScore);
//            resultMap.put("score", currentScore + textScore * 0.5);
//            // 기존 score에 searchScore를 더함
//            combinedResultsMap.put(id, resultMap);
//        }
//
//        // 결과를 리스트로 변환 및 정렬
//        List<Map<String, Object>> combinedResults
//                = new ArrayList<>(combinedResultsMap.values());
//        combinedResults.sort((a, b) -> Double.compare((double) b.get("score")
//                , (double) a.get("score")));
//
//        return combinedResults;
//    }
}
