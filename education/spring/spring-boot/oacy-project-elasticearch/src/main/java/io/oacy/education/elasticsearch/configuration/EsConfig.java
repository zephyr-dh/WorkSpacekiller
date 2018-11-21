package io.oacy.education.elasticsearch.configuration;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description
 * @auther zephyr
 * @create 2018-11-21 3:34 PM
 */

@Configuration
public class EsConfig {
    private Logger logger  = LoggerFactory.getLogger(this.getClass());

    @Bean(name = "transportClient")
    public TransportClient transportClient() throws UnknownHostException {
        /**
         * 构建一个网络接口连接地址
         */
        InetSocketTransportAddress node=new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9300);

        /**
         * 构建一个传输客户端
         */

        TransportClient client= new PreBuiltTransportClient(Settings.builder().put("cluster.name","zephyr").build());

        /**
         * 配置这个客户端
         */
        client.addTransportAddress(node);
        return client;
    }
}
