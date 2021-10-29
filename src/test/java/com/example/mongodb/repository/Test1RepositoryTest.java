package com.example.mongodb.repository;

import com.example.mongodb.domain.Test1;
import com.example.mongodb.repository.test1.Test1Repository;
import com.example.mongodb.domain.Test2;
import com.example.mongodb.repository.test2.Test2Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@SpringBootTest
class Test1RepositoryTest {

    @Autowired
    @Qualifier("primaryMongoTemplate")
    private MongoTemplate primaryTemplate;

    @Autowired
    @Qualifier("secondaryMongoTemplate")
    private MongoTemplate secondaryTemplate;

    @Autowired
    private Test1Repository test1Repository;

    @Autowired
    private Test2Repository test2Repository;

    @PostConstruct
    void init() {
//        primaryTemplate.createCollection("Test1");
//        secondaryTemplate.createCollection("Test2");
    }

    @PreDestroy
    void delete() {
        primaryTemplate.dropCollection("Test1");
        secondaryTemplate.dropCollection("Test2");
    }

    @Transactional(transactionManager = "primaryTransactionManager")
    @Test
    void test() {
        Test1 test1 = Test1.builder().title("test").build();
        test1Repository.save(test1);

        List<Test1> list1 = test1Repository.findAll();

        for (Test1 test11 : list1) {
            System.out.println("test11.getTitle() = " + test11.getTitle());
        }

        test1Repository.deleteAll();
    }

    @Transactional(transactionManager = "secondaryTransactionManager")
    @Test
    void test2() {
        Test2 test2 = Test2.builder().title("test2").build();
        test2Repository.save(test2);

        List<Test2> list2 = test2Repository.findAll();
        for (Test2 test21 : list2) {
            System.out.println("test21.getTitle() = " + test21.getTitle());
        }

        test2Repository.deleteAll();
    }
}