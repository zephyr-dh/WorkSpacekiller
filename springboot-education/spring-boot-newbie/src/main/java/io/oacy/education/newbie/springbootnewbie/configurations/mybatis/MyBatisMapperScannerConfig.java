package io.oacy.education.newbie.springbootnewbie.configurations.mybatis;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// 因为这个对象的扫描，需要在MyBatisConfiguration的后面注入，所以加上下面的注解
@AutoConfigureAfter(MyBatisConfiguration.class)
public class MyBatisMapperScannerConfig {
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //获取之前注入的beanName为sqlSessionFactory的对象
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        //指定xml配置文件的路径
        mapperScannerConfigurer.setBasePackage("io.oacy.education.newbie.springbootnewbie.repositories.mapper");
        return mapperScannerConfigurer;
    }
}