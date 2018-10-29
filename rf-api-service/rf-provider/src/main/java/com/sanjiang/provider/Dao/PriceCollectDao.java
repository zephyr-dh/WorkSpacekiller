package com.sanjiang.provider.Dao;

import com.sanjiang.provider.constrants.Constant;
import com.sanjiang.provider.domain.ShopDomain;
import com.sanjiang.provider.util.BaseCustomDao;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by byinbo on 2018/6/21.
 */
@Component
@Slf4j
public class PriceCollectDao extends BaseCustomDao{

    @Value("${spring.profiles}")
    private String profiles;

    /**
     * 采价connection
     * @return
     */
    public HikariDataSource cjDataSource() {
        ShopDomain shopDomain = new ShopDomain();
        if(profiles.equals("production")){
            // 读取默认配置文件
            shopDomain = new ShopDomain();
            shopDomain.setIP(Constant.DATABASEIPPROD);
            shopDomain.setSID(Constant.DATABASESIDPROD);
        }else{
            shopDomain.setIP(Constant.DATABASEIPDEV);
            shopDomain.setSID(Constant.DATABASESIDDEV);
        }

        // 加入连接池
        final HikariDataSource customDataConfig = new HikariDataSource();
        customDataConfig.setJdbcUrl("jdbc:oracle:thin:@" + shopDomain.getIP() + ":1521:" + shopDomain.getSID());
        customDataConfig.setUsername("sj");
        customDataConfig.setPassword("sjjxc");
        customDataConfig.setDriverClassName("oracle.jdbc.OracleDriver");
        customDataConfig.setMaximumPoolSize(2);
        customDataConfig.setMinimumIdle(1);
        customDataConfig.setAutoCommit(false);
        customDataConfig.setConnectionTestQuery("SELECT 1 FROM DUAL");
        customDataConfig.addDataSourceProperty("cachePrepStmts", "true");
        customDataConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        customDataConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return customDataConfig;
    }

}
