package com.basic.elasticsearch.services;

import com.basic.elasticsearch.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * locate com.basic.elasticsearch.services
 * Created by mastertj on 2018/3/25.
 */
//用SpringBoot的方式生成测试用例
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest("server.port:0")
public class IndexServiceTest {

    @Autowired
    IndexService indexService;

    @Test
    public void initIndexAndData() throws Exception {
        indexService.initIndexAndData("data/xiechengTour.txt");
    }

}
