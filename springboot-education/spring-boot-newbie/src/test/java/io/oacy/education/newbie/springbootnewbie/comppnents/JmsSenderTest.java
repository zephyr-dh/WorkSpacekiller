package io.oacy.education.newbie.springbootnewbie.comppnents;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("development")
@Slf4j
public class JmsSenderTest {

    @Test
    public void sendByQueue() {
    }

    @Test
    public void sendByTopic() {
    }
}