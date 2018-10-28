package io.oacy.education.newbie.springbootnewbie.configurations.customproperties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
public class CustomPropertiesConfig {
    @Value("${ds.userName}")
    private String userName;

    @Autowired
    private Environment environment;

    public void show() {
        log.info("ds.userName:{}",this.userName);
        log.info("ds.password:{}",this.environment.getProperty("ds.password"));
    }
}
