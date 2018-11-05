package io.oacy.education.xunwu.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

//说明是一个配置类
@Configuration
//配置JPA扫描的包
@EnableJpaRepositories(
//        entityManagerFactoryRef = "entityManagerFactory",
//        transactionManagerRef = "transactionManager",
        basePackages = "io.oacy.education.xunwu.repository")
//事务管理器
@EnableTransactionManagement
public class JPAConfiguration {

    //使用spring.datasource下的默认配置创建JDBC数据源
    @Bean
    //该配置默认可以不写
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    //实体类（entity）类的管理工厂
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        //选择是Hibernate作为映射实现框架 所以实例的是Hibernate
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        //是否通过实体映射建表
        jpaVendorAdapter.setGenerateDdl(false);

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        //设置工厂的数据源
        entityManagerFactoryBean.setDataSource(dataSource());
        //设置工厂的映射实现框架
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        //设置实体类的包
        entityManagerFactoryBean.setPackagesToScan("io.oacy.education.xunwu.domain");

        return entityManagerFactoryBean;
    }

    //配置一个事务管理器
    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
