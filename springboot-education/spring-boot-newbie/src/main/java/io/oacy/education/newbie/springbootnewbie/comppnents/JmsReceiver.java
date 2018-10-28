package io.oacy.education.newbie.springbootnewbie.comppnents;

import io.oacy.education.newbie.springbootnewbie.configurations.mqconfiguration.JmsConfiguration;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者使用 @JmsListener 注解监听消息。
 */
@Component
public class JmsReceiver {

    @JmsListener(destination = JmsConfiguration .QUEUE_NAME)
    public void receiveByQueue(String message) {
        System.out.println("接收队列消息:" + message);
    }

    @JmsListener(destination = JmsConfiguration .TOPIC_NAME)
    public void receiveByTopic(String message) {
        System.out.println("接收主题消息:" + message);
    }
}
