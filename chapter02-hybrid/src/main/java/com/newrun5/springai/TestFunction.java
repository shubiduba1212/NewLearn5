package com.newrun5.springai;

//import org.springframework.ai.model.function.FunctionCallbackResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

        import java.util.List;
import java.util.Map;

@Component
public class TestFunction {

    @Autowired
    private MongoSearchServiceBasic mongoSearchService;

    public ResponseEntity<List<Map<String, Object>>> searchFunction(String query) {
        // 벡터 검색과 텍스트 검색을 통합하여 결과를 반환
        List<Map<String, Object>> results = mongoSearchService.vectorSearch(query);
        return ResponseEntity.ok(results); // OK 응답을 반환
    }
}

