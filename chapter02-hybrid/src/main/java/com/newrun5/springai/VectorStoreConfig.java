package com.newrun5.springai;

import org.springframework.ai.embedding.EmbeddingModel; //임베딩 모델을 정의하는 클래스
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore; //MongoDB Atlas를 벡터 저장소로 사용하는 클래스
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore.MongoDBVectorStoreConfig; //MongoDB 벡터 저장소 설정을 구성하는 내부 클래스
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;//MongoDB와의 상호작용을 위한 템플릿 클래스

import java.util.List;

@Configuration
public class VectorStoreConfig { //MongoDB Atlas를 사용하여 벡터 저장소를 설정

    //MongoDBAtlasVectorStore 타입의 Bean을 생성하는 메서드
    @Bean
    public MongoDBAtlasVectorStore mongodbVectorStore(MongoTemplate mongoTemplate
            , EmbeddingModel embeddingModel)
    {
        MongoDBVectorStoreConfig config = MongoDBVectorStoreConfig.builder() //설정 객체를 생성하기 위한 빌더를 가져오기
                .withCollectionName("documents") //MongoDB의 컬렉션 이름을 "documents"로 설정
                .withVectorIndexName("vector_index") //벡터 인덱스의 이름을 "vector_index"로 설정
                .withPathName("embedding") //임베딩의 경로 이름을 "embedding"으로 설정
                .withMetadataFieldsToFilter(List.of("author", "type", "title", "date")) //필터링할 메타데이터 필드를 설정
                .build(); //설정 객체를 빌드

        return new MongoDBAtlasVectorStore(mongoTemplate, embeddingModel
                , config, false); // MongoDB Atlas를 사용하여 벡터 저장소를 관리
    }
}
