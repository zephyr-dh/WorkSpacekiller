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

/**
 * Created by byinbo on 2018/7/8.
 */
@Component
@Slf4j
public class ClosedAllotDao extends BaseCustomDao{

    /**
     * 完成调拨
     * @param namedParameterJdbcTemplate
     * @param scbh
     * @param bmbh
     */
    public void execFinishClosedAllot(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                       String scbh, String bmbh
    ) throws SQLException {
        final CallableStatement[] cs = {null};
        try {
            namedParameterJdbcTemplate.getJdbcOperations().execute(
                    new CallableStatementCreator() {
                        public CallableStatement createCallableStatement(Connection con) throws SQLException {
                            String storedProc = "{call Pkg_ShopClosed.P_ShopClosed_Tran(?,?)}";// 调用的sql
                            cs[0] = con.prepareCall(storedProc);
                            cs[0].setObject(1, scbh);// 设置输入参数的值
                            cs[0].setObject(2, bmbh);
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
            log.warn("完成闭店调拨失败：{}", ExceptionUtils.getMessage(e));
        }finally {
            cs[0].close();
        }
    }
}
