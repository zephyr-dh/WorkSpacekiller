package io.oacy.education.springbootnoob;

import io.oacy.education.springbootnoob.compnent.DataSourceProperties;
import io.oacy.education.springbootnoob.conditionConfiguration.EncodingConverter;
import io.oacy.education.springbootnoob.configuration.DataSourceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

@SpringBootApplication
public class SpringBootNoobApplication {

    public static void main(String[] args) {
//        SpringApplication.run(SpringBootNoobApplication.class, args);
        //获取上下文
        ConfigurableApplicationContext applicationContext=SpringApplication.run(SpringBootNoobApplication.class);
        //获得制定的Bean
        DataSourceConfiguration dataSourceConfiguration=applicationContext.getBean(DataSourceConfiguration.class);

        dataSourceConfiguration.show();
        /**
         * 测试装载自定义配置
         */
        applicationContext.getBean(DataSourceProperties.class).show();

        /**
         * 条件配置
         */
        Map<String, EncodingConverter> map = applicationContext.getBeansOfType(EncodingConverter.class);
        System.out.println(map);

    }
}
