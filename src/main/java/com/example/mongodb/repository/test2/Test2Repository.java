package com.example.mongodb.repository.test2;

import com.example.mongodb.domain.Test2;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Test2Repository extends MongoRepository<Test2, ObjectId> {
}
