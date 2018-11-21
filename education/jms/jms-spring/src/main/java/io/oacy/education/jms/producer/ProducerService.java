package io.oacy.education.jms.producer;

/**
 * @Description
 * @auther zephyr
 * @create 2018-11-19 10:52 PM
 */

public interface ProducerService {

    void sendMessage(final String message);
}
