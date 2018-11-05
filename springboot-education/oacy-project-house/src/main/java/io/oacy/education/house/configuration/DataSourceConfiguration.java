package io.oacy.education.house.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 参考文章 https://www.cnblogs.com/langdesi/articles/9101471.html
 * Created by donghua on 2018/11/5
 */
@Configuration
public class DataSourceConfiguration {

    @Bean(name = "primaryDataSource")
    @Qualifier("primaryDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDatasource() {
        return DataSourceBuilder.create().type(com.alibaba.druid.pool.DruidDataSource.class).build();
    }

    @Bean(name = "secondaryDataSource")
    @Qualifier("secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }
}
