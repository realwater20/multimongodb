package com.example.mongodb.repository;

import com.example.mongodb.repository.test1.Test1;
import com.example.mongodb.repository.test1.Test1Repository;
import com.example.mongodb.repository.test2.Test2;
import com.example.mongodb.repository.test2.Test2Repository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

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

    @Transactional(transactionManager = "primaryTransactionManager")
    @Test
    void test1() {
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

    @Transactional(transactionManager = "multiTransactionManager")
    @Test
    void test() {
        // primaryTransactionManager로 처리
        Test1 test1 = Test1.builder().title("test1").build();
        test1Repository.save(test1);
        assertEquals("test1", test1Repository.findById(test1.getId()).get().getTitle());

        // secondaryTransactionManager로 처리
        Test2 test2 = Test2.builder().title("test2").build();
        test2Repository.save(test2);
        assertEquals("test2", test2Repository.findById(test2.getId()).get().getTitle());
    }

}