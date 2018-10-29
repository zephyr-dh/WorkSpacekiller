package com.sanjiang.provider.Dao;

import com.sanjiang.provider.util.BaseCustomDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by byinbo on 2018/6/27.
 */
@Component
@Slf4j
public class LoginDao extends BaseCustomDao{

    /**
     * 登录验证
     * @param namedParameterJdbcTemplate
     * @param username
     * @param password
     * @param scbh
     * @param ip
     * @return
     */
    public String execLoginValid(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                         String username, String password, String scbh, String ip
    ) throws SQLException {
        String result = null;
        final CallableStatement[] cs = {null};
        try {
            result = (String) namedParameterJdbcTemplate.getJdbcOperations().execute(
                    new CallableStatementCreator() {
                        public CallableStatement createCallableStatement(Connection con) throws SQLException {
                            String storedProc = "{call p_rf_login(?,?,?,?,?)}";// 调用的sql
                            cs[0] = con.prepareCall(storedProc);
                            cs[0].setObject(1, username);// 设置输入参数的值
                            cs[0].setObject(2, password);
                            cs[0].setObject(3, scbh);
                            cs[0].setObject(4, ip);
                            cs[0].registerOutParameter(5, Types.VARCHAR);// 注册输出参数的类型

                            return cs[0];
                        }
                    }, new CallableStatementCallback() {
                        public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                            cs.execute();
                            String rs = cs.getString(5);

                            cs.close();
                            return rs;
                        }

                    });
        }catch (Exception e){
            log.warn("登录验证失败：{}", ExceptionUtils.getMessage(e));
        }finally {
            cs[0].close();
        }
        return result;
    }


    /**
     * 修改门店
     * @param namedParameterJdbcTemplate
     * @param scbh
     * @param ip
     * @param mac
     */
    public void execLoginConfig(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                String scbh, String ip, String mac) throws SQLException {
        final CallableStatement[] cs = {null};
        try {
            namedParameterJdbcTemplate.getJdbcOperations().execute(
                    new CallableStatementCreator() {
                        public CallableStatement createCallableStatement(Connection con) throws SQLException {
                            String storedProc = "{call p_rf_md(?,?,?)}";// 调用的sql
                            cs[0] = con.prepareCall(storedProc);

                            cs[0].setObject(1, scbh);// 设置输入参数的值
                            cs[0].setObject(2, ip);
                            cs[0].setObject(3, mac);

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
            log.warn("修改门店失败：{}", ExceptionUtils.getMessage(e));
        }finally {
            cs[0].close();

        }

    }
}
