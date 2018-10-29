package com.sanjiang.provider.Dao;

import com.sanjiang.provider.domain.collectGoods.GoodsDetail;
import com.sanjiang.provider.domain.collectGoods.PreCheckList;
import com.sanjiang.provider.util.BaseCustomDao;
import com.sanjiang.provider.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.internal.OracleTypes;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.assertj.core.util.Lists;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by byinbo on 2018/5/22.
 */
@Component
@Slf4j
public class PreCheckListDao extends BaseCustomDao{

    /**
     * 显示15天内未生成预检单
     * @param namedParameterJdbcTemplate
     * @param shopId
     * @return
     */
    public List<PreCheckList> execReportPreCheckList(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                                     String shopId) throws SQLException {
        List resultList = Lists.newArrayList();
        final CallableStatement[] cs = {null};
        try {
            resultList = (List) namedParameterJdbcTemplate.getJdbcOperations().execute(
                    new CallableStatementCreator() {
                        public CallableStatement createCallableStatement(Connection con) throws SQLException {
                            String storedProc = "{call pkg_yjd.p_report_yjd(?,?)}";// 调用的sql
                            cs[0] = con.prepareCall(storedProc);
                            cs[0].setObject(1, shopId);// 设置输入参数的值

                            cs[0].registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                            return cs[0];
                        }
                    }, new CallableStatementCallback() {
                        public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                            List resultsMap = new ArrayList();
                            cs.execute();
                            ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                            while (rs.next()) {// 转换每行的返回值到Map中
                                Map rowMap = new HashMap();
                                rowMap.put("cch", rs.getString("cch"));
                                rowMap.put("ckrq", rs.getTimestamp("ckrq"));
                                rowMap.put("scbh", rs.getString("scbh"));
                                rowMap.put("pzs", rs.getInt("pzs"));
                                rowMap.put("dps", rs.getInt("dps"));
                                rowMap.put("je", rs.getDouble("je"));
                                rowMap.put("zl", rs.getDouble("zl"));


                                resultsMap.add(rowMap);
                            }
                            rs.close();
                            cs.close();
                            return resultsMap;
                        }
                    });
        }catch (Exception e){
            log.warn("显示15天内未生成预检单失败：{}", ExceptionUtils.getMessage(e));
        }finally {
            cs[0].close();
        }

        return resultList;
    }

    /**
     * 查询预检单
     * @param namedParameterJdbcTemplate
     * @param shopId
     * @return
     */
    public List<PreCheckList> execQueryPreCheckList(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                                     String shopId, String bmbh, String cch) throws SQLException {
        List resultList = Lists.newArrayList();
        final CallableStatement[] cs = {null};
        try {
            resultList = (List) namedParameterJdbcTemplate.getJdbcOperations().execute(
                    new CallableStatementCreator() {
                        public CallableStatement createCallableStatement(Connection con) throws SQLException {
                            String storedProc = "{call pkg_yjd.p_cx_yjd(?,?,?,?)}";// 调用的sql
                            cs[0] = con.prepareCall(storedProc);
                            cs[0].setObject(1, shopId);// 设置输入参数的值
                            cs[0].setObject(2, bmbh);
                            cs[0].setObject(3, cch);

                            cs[0].registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                            return cs[0];
                        }
                    }, new CallableStatementCallback() {
                        public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                            List resultsMap = new ArrayList();
                            cs.execute();
                            ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                            while (rs.next()) {// 转换每行的返回值到Map中
                                Map rowMap = new HashMap();
                                rowMap.put("cch", rs.getString("cch"));
                                rowMap.put("ckrq", rs.getTimestamp("ckrq"));
                                rowMap.put("scbh", rs.getString("scbh"));
                                rowMap.put("pzs", rs.getInt("pzs"));
                                rowMap.put("dps", rs.getInt("dps"));
                                rowMap.put("je", rs.getDouble("je"));
                                rowMap.put("zl", rs.getDouble("zl"));


                                resultsMap.add(rowMap);
                            }
                            rs.close();
                            cs.close();
                            return resultsMap;
                        }
                    });
        }catch (Exception e){
            log.warn("查询预检单失败：{}", ExceptionUtils.getMessage(e));
        }finally {
            cs[0].close();
        }

        return resultList;
    }


    /**
     * 生成预检单
     * @param namedParameterJdbcTemplate
     * @param scbh
     * @param uuid
     * @return
     */
    public Integer execCreatePreCheckList(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                       String scbh, String uuid
    ) throws SQLException {
        Integer djdh = null;
        final CallableStatement[] cs = {null};
        try {
            djdh = (Integer) namedParameterJdbcTemplate.getJdbcOperations().execute(
                    new CallableStatementCreator() {
                        public CallableStatement createCallableStatement(Connection con) throws SQLException {
                            String storedProc = "{call pkg_yjd.p_sc_yjd(?,?,?)}";// 调用的sql
                            cs[0] = con.prepareCall(storedProc);
                            cs[0].setObject(1, uuid);// 设置输入参数的值
                            cs[0].setObject(2, scbh);

                            cs[0].registerOutParameter(3, Types.INTEGER);// 注册输出参数的类型
                            return cs[0];
                        }
                    }, new CallableStatementCallback() {
                        public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                            cs.execute();
                            Integer rs = cs.getInt(3);

                            cs.close();
                            return rs;
                        }

                    });
        }catch (Exception e){
            log.warn("生成预检单失败：{}", ExceptionUtils.getMessage(e));
        }finally {
            cs[0].close();
        }
        return djdh;
    }

    /**
     * 验货明细列表
     * @param namedParameterJdbcTemplate
     * @param shopId
     * @param djdh
     * @return
     */
    public List<GoodsDetail> execInspectGoodsList(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                                  String shopId, String djdh) throws SQLException {
        List resultList = Lists.newArrayList();
        final CallableStatement[] cs = {null};
        try {
            resultList = (List) namedParameterJdbcTemplate.getJdbcOperations().execute(
                    new CallableStatementCreator() {
                        public CallableStatement createCallableStatement(Connection con) throws SQLException {
                            String storedProc = "{call pkg_yjd.p_cxyh_yjd(?,?,?)}";// 调用的sql
                            cs[0] = con.prepareCall(storedProc);
                            cs[0].setObject(1, djdh);// 设置输入参数的值
                            cs[0].setObject(2, shopId);

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
                                rowMap.put("djdh", rs.getString("djdh"));
                                rowMap.put("glbh", rs.getString("glbh"));
                                rowMap.put("spbh", rs.getString("spbh"));
                                rowMap.put("spmc", rs.getString("spmc"));
                                rowMap.put("dhgg", rs.getString("dhgg"));
                                rowMap.put("yss", rs.getString("yss"));
                                rowMap.put("ys", rs.getString("ys"));
//                            rowMap.put("sssl", rs.getString("sssl"));
//                            rowMap.put("ssjs", rs.getString("ssjs"));
//                            rowMap.put("cysl", rs.getString("cysl"));
//                            rowMap.put("cyjs", rs.getString("cyjs"));
                                rowMap.put("djly", rs.getString("djly"));
                                rowMap.put("spjj", String.format("%.2f", rs.getDouble("spjj")));
                                rowMap.put("kcsl", rs.getString("kcsl"));
                                rowMap.put("ckrq", rs.getTimestamp("ckrq"));

                                resultsMap.add(rowMap);
                            }
                            rs.close();
                            cs.close();
                            return resultsMap;
                        }
                    });
        }catch (Exception e){
            log.warn("验货明细列表失败：{}", ExceptionUtils.getMessage(e));
        }finally {
            cs[0].close();
        }

        return resultList;
    }


    /**
     * 审核
     * @param namedParameterJdbcTemplate
     * @param scbh
     * @param djdh
     */
    public void execVerifyPreCheckList(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                       String scbh, String djdh
    ) throws SQLException {
        final CallableStatement[] cs = {null};
        try {
            namedParameterJdbcTemplate.getJdbcOperations().execute(
                    new CallableStatementCreator() {
                        public CallableStatement createCallableStatement(Connection con) throws SQLException {
                            String storedProc = "{call pkg_yjd.p_sh_yjd(?,?)}";// 调用的sql
                            cs[0] = con.prepareCall(storedProc);

                            cs[0].setObject(1, djdh);// 设置输入参数的值
                            cs[0].setObject(2, scbh);

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
            log.warn("审核失败：{}", ExceptionUtils.getMessage(e));
        }finally {
            cs[0].close();
        }
    }


}
