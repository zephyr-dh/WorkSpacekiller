package io.oacy.education.newbie.springbootnewbie;

import io.oacy.education.newbie.springbootnewbie.configurations.customproperties.CustomPropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringBootNewbieApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootNewbieApplication.class, args);
    }

//    /**
//     * 加载 ApplicationContext
//     * @param args
//     */
//    public static void main(String[] args) {
//        ConfigurableApplicationContext context = SpringApplication.run(SpringBootNewbieApplication.class, args);
//        context.getBean(CustomPropertiesConfig.class).show();
//    }
}
