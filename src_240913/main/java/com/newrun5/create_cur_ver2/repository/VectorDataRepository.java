package com.newrun5.create_cur_ver2.repository;

import com.newrun5.create_cur_ver2.model.VectorData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VectorDataRepository extends MongoRepository<VectorData, String> {
}

