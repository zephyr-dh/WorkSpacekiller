package io.oacy.education.jms.producer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.stream.IntStream;

/**
 * @Description
 * @auther zephyr
 * @create 2018-11-19 11:21 PM
 */

public class ApplicationProducer {

    public static void main(String[] args) {
        ApplicationContext context=new ClassPathXmlApplicationContext("classpath:producer.xml");
        ProducerService producerService=context.getBean(ProducerService.class);

        IntStream.range(0, 100).mapToObj(i -> "message:" + i).forEach(producerService::sendMessage);

        ((ClassPathXmlApplicationContext) context).close();
    }
}
