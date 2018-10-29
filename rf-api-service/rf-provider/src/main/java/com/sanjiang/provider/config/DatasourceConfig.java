package com.sanjiang.provider.config;

import com.sanjiang.provider.filter.SqlCostInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * rf枪数据源
 *
 * @author kimiyu
 * @date 2018/4/16 09:35
 */
@Configuration
@MapperScan(basePackages = DatasourceConfig.PACKAGE, sqlSessionFactoryRef = "rfSqlSessionFactory")
public class DatasourceConfig {

    static final String PACKAGE = "com.sanjiang.provider.mapper.rf";

    static final String DOMAIN = "com.sanjiang.provider.domain";

    static final String LOCATION = "classpath:mapper/rf/*.xml";

    @Bean(name = "rfDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    @Primary
    public DataSource rfDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "rfJdbcTemplate")
    @Primary
    public JdbcTemplate edbJdbcTempate(@Qualifier("rfDataSource") DataSource edbDataSource) {
        return new JdbcTemplate(edbDataSource);
    }

    @Bean(name = "rfTransactionManager")
    @Primary
    public DataSourceTransactionManager rfTransactionManager(@Qualifier("rfDataSource") DataSource edbDataSource) {
        return new DataSourceTransactionManager(edbDataSource);
    }

    @Bean(name = "rfSqlSessionFactory")
    @Primary
    public SqlSessionFactory rfSqlSessionFactory(@Qualifier("rfDataSource") DataSource edbDataSource) throws Exception {
        SqlSessionFactoryBean edbSessionFactory = new SqlSessionFactoryBean();
        edbSessionFactory.setDataSource(edbDataSource);
        edbSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(DatasourceConfig.LOCATION));
        edbSessionFactory.setTypeAliasesPackage(DOMAIN);

        // 配置拦截插件
        edbSessionFactory.setPlugins(new Interceptor[]{
                new SqlCostInterceptor()
        });
        return edbSessionFactory.getObject();
    }
}
