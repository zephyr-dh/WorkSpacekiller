package com.sanjiang.provider.util;

import com.sanjiang.provider.constrants.Constant;
import com.sanjiang.provider.domain.ShopDomain;
import com.sanjiang.provider.domain.goodsoutoftime.PreWarnShelfLife;
import com.sanjiang.provider.service.ShopService;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库连接池
 *
 * @author kimiyu
 * @date 2018/4/16 15:20
 */
@Component
@Slf4j
public class BaseCustomDao {

    @Autowired
    private ShopService shopService;

    @Value("${spring.profiles}")
    private String profiles;

    /**
     * 动态生成Connection
     *
     * @param shopId
     * @return
     * @throws SQLException
     */
    public HikariDataSource customDataSource(String shopId) {
        ShopDomain shopDomain = new ShopDomain();
        if(profiles.equals("production")){
            shopDomain = shopService.getByShopId(shopId);
            if (null == shopDomain) {
                // 读取默认配置文件
                shopDomain = new ShopDomain();
                shopDomain.setIP(Constant.DATABASEIPPROD);
                shopDomain.setSID(Constant.DATABASESIDPROD);
            }
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



    /**
     * 处理插入、更新或删除sql
     *
     * @param namedParameterJdbcTemplate
     * @param sqlParameterSource
     * @param dataSource
     * @param sql
     * @return
     */
    public Integer operateSQL(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                              SqlParameterSource sqlParameterSource,
                              DataSource dataSource,
                              String sql) {
        TransactionTemplate transactionTemplate = getTransactionTemplate(dataSource);
        return transactionTemplate.execute(status -> {
            try {
                return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
            } catch (Exception ex) {
                status.setRollbackOnly();
                log.info("执行SQL异常：{}", ExceptionUtils.getMessage(ex));
                return null;
            }
        });
    }

    /**
     * 处理插入、更新或删除sql
     *
     * @param namedParameterJdbcTemplate
     * @param sqlParameterSource
     * @param dataSource
     * @param sql
     * @return
     */
    public int[] batchSQL(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                         SqlParameterSource[] sqlParameterSource,
                         DataSource dataSource,
                         String sql) {
        TransactionTemplate transactionTemplate = getTransactionTemplate(dataSource);
        return transactionTemplate.execute(status -> {
            try {
                return namedParameterJdbcTemplate.batchUpdate(sql, sqlParameterSource);
            } catch (Exception ex) {
                status.setRollbackOnly();
                log.info("执行SQL异常：{}", ExceptionUtils.getMessage(ex));
                return null;
            }
        });
    }


    /**
     * 获取事务模板
     *
     * @param dataSource
     * @return
     */
    public TransactionTemplate getTransactionTemplate(DataSource dataSource) {
        PlatformTransactionManager txManager = new DataSourceTransactionManager(
                dataSource);
        return new TransactionTemplate(txManager);
    }

    /**
     * 执行门店的存储过程
     *
     * @param namedParameterJdbcTemplate
     * @param shopId
     * @throws SQLException
     */
    public void execScbh(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String shopId) throws SQLException {
        // 执行存储过程
        String sql = "{call p_scbh(:as_shopId)}";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("as_shopId", shopId);
        namedParameterJdbcTemplate.execute(sql, paramMap, PreparedStatement::execute);
    }

    /**
     * 执行保质期预警查询
     * @param namedParameterJdbcTemplate
     * @param report
     * @param shopId
     */
    public List<PreWarnShelfLife> execGoodsOurOfTimeCx(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                                       String report,
                                                       String shopId,
                                                       String jcrq,
                                                       String fzm,
                                                       String spbh) throws SQLException{
        List resultList = Lists.newArrayList();
        final CallableStatement[] cs = {null};
        try {
            resultList = (List) namedParameterJdbcTemplate.getJdbcOperations().execute(
                    new CallableStatementCreator() {
                        public CallableStatement createCallableStatement(Connection con) throws SQLException {

                            java.sql.Timestamp jcDate = null;
                            if (StringUtils.isNotEmpty(jcrq)) {
                                jcDate = new java.sql.Timestamp(DateUtil.parse(jcrq, DateUtil.DEFAULT_DATE_TIME_FORMAT).getTime());
                            }
                            String storedProc = "{call pkg_goodsoutoftime.p_goodsoutoftime_cx(?,?,?,?,?,?)}";// 调用的sql
                            cs[0] = con.prepareCall(storedProc);
                            cs[0].setObject(1, report);// 设置输入参数的值
                            cs[0].setObject(2, shopId);
                            cs[0].setObject(4, jcDate);
                            cs[0].setObject(5, fzm);
                            cs[0].setObject(6, spbh);
                            cs[0].registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型

                            return cs[0];
                        }
                    }, new CallableStatementCallback() {
                        public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                            List resultsMap = new ArrayList();
                            cs.execute();
                            ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                            while (rs.next()) {// 转换每行的返回值到Map中
                                Map rowMap = new HashMap();
                                if (report.equals(PreWarnShelfLifeEnum.HZCX.getReport())) {
                                    rowMap.put("jcrq", rs.getTimestamp("jcrq"));
                                    rowMap.put("cnt", rs.getString("cnt"));
                                    rowMap.put("yjccnt", rs.getString("yjccnt"));
                                    rowMap.put("fzm", rs.getString("fzm"));
                                } else if (report.equals(PreWarnShelfLifeEnum.YJDSCFZM.getReport())) {
                                    rowMap.put("fzmc", rs.getString("fzmc"));
                                    rowMap.put("fzlx", rs.getString("fzlx"));
                                    rowMap.put("deptlevelid", rs.getString("deptlevelid"));
                                    rowMap.put("fzm", rs.getString("fzm"));
                                } else if (report.equals(PreWarnShelfLifeEnum.YJDJCFZM.getReport())) {
                                    rowMap.put("fzm", rs.getString("fzm"));
                                    rowMap.put("fzmc", rs.getString("fzmc"));
                                    rowMap.put("cnt", rs.getString("cnt"));
                                    rowMap.put("yjccnt", rs.getString("yjccnt"));
                                    rowMap.put("yjczb", rs.getString("yjczb"));
                                } else if (report.equals(PreWarnShelfLifeEnum.YJDJCSP.getReport())) {
                                    rowMap.put("spmc", rs.getString("spmc"));
                                    rowMap.put("hjbh", rs.getString("hjbh"));
                                    rowMap.put("spbh", rs.getString("spbh"));
                                    rowMap.put("glbh", rs.getString("glbh"));
                                    rowMap.put("bzqts", rs.getString("bzqts"));
                                    rowMap.put("jzhyj", String.format("%.2f", rs.getDouble("jzhyj")));
                                    rowMap.put("kcsl", rs.getString("kcsl"));
                                } else if (report.equals(PreWarnShelfLifeEnum.YJDDPJC.getReport())) {
                                    rowMap.put("spmc", rs.getString("spmc"));
                                    rowMap.put("jzhyj", String.format("%.2f", rs.getDouble("jzhyj")));
                                    rowMap.put("kcsl", rs.getString("kcsl"));
                                    rowMap.put("glbh", rs.getString("glbh"));
                                    rowMap.put("bzqts", rs.getString("bzqts"));
                                } else if (report.equals(PreWarnShelfLifeEnum.YJDSPSCRQ.getReport())) {
                                    rowMap.put("scrq", rs.getTimestamp("scrq"));
                                    rowMap.put("clrq", rs.getTimestamp("clrq"));
                                } else if (report.equals(PreWarnShelfLifeEnum.YJDSPHJBH.getReport())) {
                                    rowMap.put("hjbh", rs.getString("hjbh"));
                                }

                                resultsMap.add(rowMap);
                            }
                            rs.close();
                            cs.close();
                            return resultsMap;
                        }
                    });
        }catch (Exception e){
            log.warn("执行保质期预警查询失败：{}", ExceptionUtils.getMessage(e));
        }finally {
            cs[0].close();
        }

        return resultList;
    }

    /**
     * 生成预警单
     * @param namedParameterJdbcTemplate
     * @param shopId
     * @param fzm
     * @return
     */
    public Timestamp execGoodsOutOfTime(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                   String shopId,
                                   String[] fzm
                                   ) throws SQLException {
        Timestamp jcrq = null;
        final CallableStatement[] cs = {null};
        try {
            jcrq = (Timestamp) namedParameterJdbcTemplate.getJdbcOperations().execute(
                    new CallableStatementCreator() {
                        public CallableStatement createCallableStatement(Connection conH) throws SQLException {
                            OracleConnection con = conH.unwrap(oracle.jdbc.driver.OracleConnection.class);
                            String storedProc = "{call pkg_goodsoutoftime.p_goodsoutoftime(?,?,?)}";// 调用的sql
                            cs[0] = con.prepareCall(storedProc);
//                        String array[] = {"50"};
                            ArrayDescriptor des = ArrayDescriptor.createDescriptor("VARTABLETYPE",
                                    con);
                            ARRAY array_to_pass = new ARRAY(des, con, fzm);
                            cs[0].setObject(1, shopId);// 设置输入参数的值
                            cs[0].setArray(2, array_to_pass);
                            cs[0].registerOutParameter(3, OracleTypes.TIMESTAMP);// 注册输出参数的类型
                            return cs[0];
                        }
                    }, new CallableStatementCallback() {
                        public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                            cs.execute();
                            Timestamp rq = cs.getTimestamp(3);
                            cs.close();
                            return rq;
                        }

                    });
        }catch (Exception e){
            log.warn("生成预警单失败：{}", ExceptionUtils.getMessage(e));
        }finally {
            cs[0].close();
        }
        return jcrq;
    }

    /**
     * 商品检查
     * @param namedParameterJdbcTemplate
     * @param czlx
     * @param shopId
     * @param glbh
     * @param scrq
     * @param hjbh
     * @param clry
     * @param jcrq
     */
    public void execGoodsOutOfTimeCz(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                     String czlx,
                                     String shopId,
                                     String glbh,
                                     String scrq,
                                     String hjbh,
                                     String clry,
                                     String jcrq
    ) throws SQLException {
        final CallableStatement[] cs = {null};
        try {
            namedParameterJdbcTemplate.getJdbcOperations().execute(
                    new CallableStatementCreator() {
                        public CallableStatement createCallableStatement(Connection con) throws SQLException {
                            java.sql.Timestamp jcDate = null;
                            if (StringUtils.isNotEmpty(jcrq)) {
                                jcDate = new java.sql.Timestamp(DateUtil.parse(jcrq, DateUtil.DEFAULT_DATE_TIME_FORMAT).getTime());
                            }
                            java.sql.Timestamp scDate = null;
                            if (StringUtils.isNotEmpty(scrq)) {
                                scDate = new java.sql.Timestamp(DateUtil.parse(scrq, DateUtil.DEFAULT_DATE_FORMAT).getTime());
                            }
                            String storedProc = "{call pkg_goodsoutoftime.p_goodsoutoftime_cz(?,?,?,?,?,?,?)}";// 调用的sql
                            cs[0] = con.prepareCall(storedProc);

                            cs[0].setObject(1, czlx);// 设置输入参数的值
                            cs[0].setObject(2, shopId);
                            cs[0].setObject(3, glbh);
                            cs[0].setObject(4, scDate);
                            cs[0].setObject(5, hjbh);
                            cs[0].setObject(6, clry);
                            cs[0].setObject(7, jcDate);

                            return cs[0];
                        }
                    }, new CallableStatementCallback() {
                        public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                            cs.execute();
                            cs.close();
                            return null;
                        }

                    });
        }catch (Exception e){
            log.warn("商品检查失败：{}", ExceptionUtils.getMessage(e));
        }finally {
            cs[0].close();
        }
    }




}
