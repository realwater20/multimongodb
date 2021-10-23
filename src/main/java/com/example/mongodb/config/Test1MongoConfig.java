package com.example.mongodb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author Marcos Barbero
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.example.mongodb.repository.test1",
        mongoTemplateRef = Test1MongoConfig.MONGO_TEMPLATE)
public class Test1MongoConfig {

    protected static final String MONGO_TEMPLATE = "primaryMongoTemplate";
}
