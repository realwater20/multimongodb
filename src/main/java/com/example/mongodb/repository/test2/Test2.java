package com.example.mongodb.repository.test2;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@Document(collection = "Primary")
public class Test2 {

    @Id
    private ObjectId id;

    private String title;

}
