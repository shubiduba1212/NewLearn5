package com.newrun5.create_cur_ver2.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.newrun5.create_cur_ver2.model.DataItem;
import org.springframework.stereotype.Repository;

@Repository
public interface DataItemRepository extends MongoRepository<DataItem, String> {
    // 커스텀 쿼리 메소드 추가 가능
}

