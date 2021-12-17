package com.example.mongodb.repository.test1;

import com.example.mongodb.domain.Test1;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Test1Repository extends MongoRepository<Test1, ObjectId> {
}
