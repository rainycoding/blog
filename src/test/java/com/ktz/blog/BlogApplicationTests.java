package com.ktz.blog;

import com.ktz.blog.dao.CategoryRepository;
import com.ktz.blog.entity.Category;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BlogApplicationTests {

    private Logger LOGGER = LoggerFactory.getLogger(BlogApplicationTests.class);

    @Test
    void contextLoads() {
        LOGGER.info("你好");
    }

}
