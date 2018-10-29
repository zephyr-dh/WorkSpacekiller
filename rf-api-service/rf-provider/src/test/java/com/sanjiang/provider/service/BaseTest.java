package com.sanjiang.provider.service;

import com.sanjiang.provider.config.DatasourceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author kimiyu
 * @date 2018/4/16 14:17
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Import(value = {DatasourceConfig.class})
@ActiveProfiles(value = {"dev"})
public class BaseTest {

    @Test
    public void beginTest() {
        System.out.println("初始化数据！");
    }
}
