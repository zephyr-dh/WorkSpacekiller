package io.oacy.education.jms.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @Description
 * @auther zephyr
 * @create 2018-11-19 11:43 PM
 */

public class ConsumerMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        TextMessage tm= (TextMessage) message;

        try {
            System.out.println("got message:"+tm.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
