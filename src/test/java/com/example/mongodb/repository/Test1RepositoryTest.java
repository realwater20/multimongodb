package com.example.mongodb.repository;

import com.example.mongodb.repository.test1.Test1;
import com.example.mongodb.repository.test1.Test1Repository;
import com.example.mongodb.repository.test2.Test2;
import com.example.mongodb.repository.test2.Test2Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class Test1RepositoryTest {

    @Autowired
    private Test1Repository test1Repository;

    @Autowired
    private Test2Repository test2Repository;

    @Test
    void test() {
        Test1 test1 = Test1.builder().title("test").build();
        test1Repository.save(test1);

        Test2 test2 = Test2.builder().title("test2").build();
        test2Repository.save(test2);

        List<Test1> list1 = test1Repository.findAll();

        for (Test1 test11 : list1) {
            System.out.println("test11.getTitle() = " + test11.getTitle());
        }

        List<Test2> list2 = test2Repository.findAll();
        for (Test2 test21 : list2) {
            System.out.println("test21.getTitle() = " + test21.getTitle());
        }

    }
}