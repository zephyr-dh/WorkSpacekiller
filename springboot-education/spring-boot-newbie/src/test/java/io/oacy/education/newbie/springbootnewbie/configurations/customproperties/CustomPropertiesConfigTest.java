package io.oacy.education.newbie.springbootnewbie.configurations.customproperties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("development")
public class CustomPropertiesConfigTest {

    @Test
    public void show() {
    }
}