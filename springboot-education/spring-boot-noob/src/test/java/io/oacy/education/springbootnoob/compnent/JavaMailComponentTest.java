package io.oacy.education.springbootnoob.compnent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;



@RunWith(SpringRunner.class)
@SpringBootTest
@Profile("development")
public class JavaMailComponentTest {

    @Autowired
    private JavaMailComponent javaMailComponent;

    @BeforeMethod
    public void setUp() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~测试开始~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~测试结束~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Test
    public void testSendMail() {

        javaMailComponent.sendMail("zephyr@oacy.io");
    }
}