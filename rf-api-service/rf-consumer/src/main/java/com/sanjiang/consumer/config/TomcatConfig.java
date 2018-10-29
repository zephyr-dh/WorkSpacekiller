package com.sanjiang.consumer.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * tomcat配置
 * 解决tomcat无法操作DELETE的问题
 *
 * @author kimiyu
 * @date 2018/5/3 10:24
 */
@Configuration
public class TomcatConfig {

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
        return new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected void customizeConnector(Connector connector) {
                super.customizeConnector(connector);
                connector.setParseBodyMethods("PUT,DELETE");
            }
        };
    }
}
