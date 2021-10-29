package com.example.mongodb;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Profile("local")
@Configuration
public class LocalCommandRunner implements CommandLineRunner {

    private final MongoTemplate primaryMongoTemplate;

    private final MongoTemplate secondaryMongoTemplate;

    public LocalCommandRunner(@Qualifier("primaryMongoTemplate") MongoTemplate primaryMongoTemplate,
                              @Qualifier("secondaryMongoTemplate") MongoTemplate secondaryMongoTemplate) {
        this.primaryMongoTemplate = primaryMongoTemplate;
        this.secondaryMongoTemplate = secondaryMongoTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
//        primaryMongoTemplate.createCollection("Test1");
//        secondaryMongoTemplate.createCollection("Test2");
    }

}
