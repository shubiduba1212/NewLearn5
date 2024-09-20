package com.newrun5.save_pdf.repository;

import com.newrun5.save_pdf.model.DataItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DataItemRepository extends MongoRepository<DataItem, String> {
    // 'content' 필드에서 지정한 키워드가 포함된 결과를 찾는 메서드
    List<DataItem> findByContentContaining(String keyword);
}


