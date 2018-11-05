package io.oacy.education.house.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by donghua on 2018/11/5
 */
@Configuration
//@ConfigurationProperties(prefix = "spring.datasource")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "io.oacy.education.house.repository")
//@Data
public class JpaConfiguration {
//
//    private String url;
//    private String username;
//    private String password;
//    private String driverClassName;
//    private int initialSize;
//    private int minIdle;
//    private int maxActive;
//    private int maxWait;
//    private int timeBetweenEvictionRunsMillis;
//    private int minEvictableIdleTimeMillis;
//    private String validationQuery;
//    private boolean testWhileIdle;
//    private boolean testOnBorrow;
//    private boolean testOnReturn;
//    private boolean poolPreparedStatements;
//    private int maxPoolPreparedStatementPerConnectionSize;
//    private String filters;
//    private String connectionProperties;

//    @Bean(name = "dataSource")     //声明其为Bean实例
//    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
//    public DataSource dataSource() {
//        DruidDataSource datasource = new DruidDataSource();
//        datasource.setUrl(url);
//        datasource.setUsername(username);
//        datasource.setPassword(password);
//        datasource.setDriverClassName(driverClassName);
//
//        //configuration
//        datasource.setInitialSize(initialSize);
//        datasource.setMinIdle(minIdle);
//        datasource.setMaxActive(maxActive);
//        datasource.setMaxWait(maxWait);
//        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
//        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
//        datasource.setValidationQuery(validationQuery);
//        datasource.setTestWhileIdle(testWhileIdle);
//        datasource.setTestOnBorrow(testOnBorrow);
//        datasource.setTestOnReturn(testOnReturn);
//        datasource.setPoolPreparedStatements(poolPreparedStatements);
//        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
//        try {
//            datasource.setFilters(filters);
//        } catch (SQLException e) {
//            System.err.println("druid configuration initialization filter: " + e);
//        }
//        datasource.setConnectionProperties(connectionProperties);
//        return datasource;
//    }

    @Bean
    @ConditionalOnBean(DataSource.class)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("primaryDataSource")DataSource dataSource) {
        HibernateJpaVendorAdapter japVendor = new HibernateJpaVendorAdapter();
        japVendor.setGenerateDdl(false);

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
//        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setJpaVendorAdapter(japVendor);
        entityManagerFactory.setPackagesToScan("io.oacy.education.house.domain");
        return entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

}
