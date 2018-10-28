package io.oacy.education.newbie.springbootnewbie.configurations.datasource;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import com.alibaba.druid.filter.Filter;


//@Configuration
//public class DruidConfiguration {
//
//    @ConfigurationProperties(prefix = "spring.datasource.druid")
//    @Bean(name = "dataSource",destroyMethod = "close", initMethod = "init")
//    public DruidDataSource dataSource() {
//        DruidDataSource ds = new DruidDataSource();
//        ds.setProxyFilters(Arrays.asList(statFilter()));
//        return ds;
//    }
//
//    @Bean
//    public Filter statFilter() {
//        StatFilter filter = new StatFilter();
//        filter.setSlowSqlMillis(5000);
//        filter.setLogSlowSql(true);
//        filter.setMergeSql(true);
//        return filter;
//    }
//}
