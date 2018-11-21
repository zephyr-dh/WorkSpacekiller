package io.oacy.education.jms.consumer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Description
 * @auther zephyr
 * @create 2018-11-19 11:50 PM
 */

public class ApplicationConsumer {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:producer.xml");
    }
}
