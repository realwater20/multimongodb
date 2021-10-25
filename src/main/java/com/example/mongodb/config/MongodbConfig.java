package com.example.mongodb.config;

import de.flapdoodle.embed.mongo.config.*;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.SessionSynchronization;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.io.IOException;

@Configuration
public class MongodbConfig {

    private String mongodb_uri1 = "mongodb://localhost:11046/test1?replicaSet=emb&readPreference=primary&ssl=false";

    private String mongodb_uri2 = "mongodb://localhost:11046/test2?replicaSet=emb&readPreference=primary&ssl=false";

    @Bean(name = "primaryMongoTemplate")
    @Primary
    public MongoTemplate primaryMongoTemplate() {
        MongoDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(mongodb_uri1);
        MappingMongoConverter converter = new MappingMongoConverter(
                new DefaultDbRefResolver(factory),
                new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        MongoTemplate mongoTemplate = new MongoTemplate(factory, converter);
        mongoTemplate.setSessionSynchronization(SessionSynchronization.ALWAYS);
        return mongoTemplate;
    }

    @Bean(name = "secondaryMongoTemplate")
    public MongoTemplate secondaryMongoTemplate() {
        MongoDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(mongodb_uri2);
        MappingMongoConverter converter = new MappingMongoConverter(
                new DefaultDbRefResolver(factory),
                new MongoMappingContext());
        MongoTemplate mongoTemplate = new MongoTemplate(factory, converter);
        mongoTemplate.setSessionSynchronization(SessionSynchronization.ALWAYS);
        return mongoTemplate;
    }

    @Primary
    @Bean("primaryTransactionManager")
    public MongoTransactionManager primaryTransactionManager() {
        return new MongoTransactionManager(new SimpleMongoClientDatabaseFactory(mongodb_uri1));
    }

    @Bean("secondaryTransactionManager")
    public MongoTransactionManager secondaryTransactionManager() {
        return new MongoTransactionManager(new SimpleMongoClientDatabaseFactory(mongodb_uri2));
    }
}
