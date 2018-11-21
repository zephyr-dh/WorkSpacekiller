package io.oacy.education.jms.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.Resource;
import javax.jms.Destination;

/**
 * @Description
 * @auther zephyr
 * @create 2018-11-19 11:04 PM
 */

public class ProducerServiceImpl implements ProducerService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource(name = "queueDestination")
//    主题模式
//    @Resource(name = "topicDestination")
    private Destination destination;

    public void sendMessage(final String message){
        jmsTemplate.send(destination,(session)-> session.createTextMessage(message));
        System.out.println("send message:"+message);
    }
}
