package com.sanjiang.provider.Dao;

import com.sanjiang.provider.util.BaseCustomDao;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleTypes;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

/**
 * Created by wangpan on 2018/08/02.
 */
@Component
@Slf4j
public class PreAllotDao extends BaseCustomDao{

    /**
     * 订货时查询商品信息
     * @param namedParameterJdbcTemplate
     * @param storeId
     * @param otherStoreId
     * @param goodCode
     * @return
     * @throws SQLException
     */
    public List execOrderGood(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                            String storeId, String otherStoreId, String goodCode) throws SQLException {
        log.info("门店：{} 订货商品：{}",storeId,goodCode);
        List result = null;
        final CallableStatement[] cs = {null};
        try {
            result = (List) namedParameterJdbcTemplate.getJdbcOperations().execute(
                    new CallableStatementCreator() {
                        public CallableStatement createCallableStatement(Connection con) throws SQLException {
                            String storedProc = "{call Pkg_Prepostion.Pkg_Prepostion_cx('spcx',?,?,?,?,null)}";// 调用的sql
                            cs[0] = con.prepareCall(storedProc);
                            cs[0].setString(1, storeId);// 设置输入参数的值
                            cs[0].registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                            cs[0].setString(3, otherStoreId);
                            cs[0].setString(4, goodCode);

                            return cs[0];
                        }
                    }, new CallableStatementCallback() {
                        public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                            List resultMap = new ArrayList();
                            cs.execute();
                            ResultSet rs = (ResultSet) cs.getObject(2);
                            while (rs.next()) {
                                Map rowMap = new HashMap();
                                rowMap.put("glbh", rs.getString("glbh"));
                                rowMap.put("spbh", rs.getString("spbh"));
                                rowMap.put("spmc", rs.getString("spmc"));
                                rowMap.put("dhgg", rs.getString("dhgg"));
                                rowMap.put("kcsl", rs.getInt("kcsl"));
                                rowMap.put("spztmc", rs.getString("spztmc"));
                                rowMap.put("xssl", rs.getInt("xssl"));
                                rowMap.put("xl28", rs.getInt("xl28"));
                                rowMap.put("splx", rs.getString("splx"));
                                rowMap.put("dhsl", rs.getInt("dhsl"));
                                rowMap.put("kcsl_df", rs.getInt("kcsl_df"));

                                resultMap.add(rowMap);
                            }
                            rs.close();
                            return resultMap;

                        }

                    });
        }catch (Exception e){
            log.error("调用存储过程失败：{}", ExceptionUtils.getMessage(e));
        }finally {
            cs[0].close();
        }
        return result;
    }


    /**
     * 查询调出的有效订单
     * @param jdbcTemplate
     * @param store
     * @return
     */
    public List getValidOrder(NamedParameterJdbcTemplate jdbcTemplate,String store) throws SQLException {
        log.info("门店：{} 查询有效的调出订单",store);

        List result = null;
        final CallableStatement[] cs = {null};

        try{
            result = (List) jdbcTemplate.getJdbcOperations().execute(new CallableStatementCreator(){
                public CallableStatement createCallableStatement(Connection conn) throws SQLException{
                    String storedProc = "{call Pkg_Prepostion.Pkg_Prepostion_cx('dcwfj',?,?)}";
                    cs[0] = conn.prepareCall(storedProc);
                    cs[0].setString(1,store);
                    cs[0].registerOutParameter(2,OracleTypes.CURSOR);

                    return cs[0];
                }
            }, new CallableStatementCallback() {
                @Override
                public Object doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                    List resultMap = new ArrayList();

                    callableStatement.execute();
                    ResultSet rs = (ResultSet) callableStatement.getObject(2);
                    while (rs.next()){
                        Map temp = new HashMap();
                        temp.put("lx",rs.getString("lx"));
                        temp.put("scbh",rs.getString("scbh"));
                        temp.put("bmjc",rs.getString("bmjc"));
                        temp.put("zdrq",rs.getString("zdrq"));
                        temp.put("djh",rs.getString("djh"));
                        temp.put("cnt",rs.getInt("cnt"));
                        temp.put("zt",rs.getString("zt"));

                        resultMap.add(temp);
                    }

                    rs.close();
                    callableStatement.close();

                    return resultMap;
                }
            });

        }catch (Exception e){
            log.error("调用存储过程失败：{}", ExceptionUtils.getMessage(e));
        }finally {
            cs[0].close();
        }
        return result;
    }

    /**
     * 查询调出订单中的商品信息
     * @param jdbcTemplate
     * @param storeId
     * @param orderId
     * @return
     */
    public List getPreOutGoods(NamedParameterJdbcTemplate jdbcTemplate,String storeId,String orderId) throws SQLException {
        log.info("门店：{} 查询订单：{} 中的商品",storeId,orderId);

        List result = null;
        final CallableStatement[] cs = {null};

        try{
            result  = (List)jdbcTemplate.getJdbcOperations().execute(new CallableStatementCreator() {
                public CallableStatement createCallableStatement(Connection conn) throws SQLException {
                    String storeProc = "{call Pkg_Prepostion.Pkg_Prepostion_cx('ddspcx',?,?,null,null,?)}";
                    cs[0] = conn.prepareCall(storeProc);
                    cs[0].setString(1,storeId);
                    cs[0].registerOutParameter(2,OracleTypes.CURSOR);
                    cs[0].setString(3,orderId);

                    return cs[0];
                }
            }, new CallableStatementCallback() {
                @Override
                public Object doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                    List resultMap = new ArrayList();
                    callableStatement.execute();

                    ResultSet rs = (ResultSet)callableStatement.getObject(2);
                    while (rs.next()){
                        Map temp = new HashMap();
                        temp.put("glbh",rs.getString("glbh"));
                        temp.put("djh",rs.getString("djh"));
                        temp.put("spmc",rs.getString("spmc"));
                        temp.put("spbh",rs.getString("spbh"));
                        temp.put("hjbh",rs.getString("hjbh"));
                        temp.put("shsl",rs.getString("shsl"));
                        temp.put("sdsl",rs.getString("sdsl"));

                        resultMap.add(temp);
                    }

                    return resultMap;
                }
            });
        }catch(Exception e){
            log.error("调用存储过程失败：{}", ExceptionUtils.getMessage(e));

        }finally {
            cs[0].close();
        }
        return result;
    }

    /**
     * 拣货时扫描商品条码判断是否可以调拨
     * @param jdbcTemplate
     * @param storeId
     * @param goodCode
     * @return
     */
    public Integer queryGoodIsAllot(NamedParameterJdbcTemplate jdbcTemplate,String storeId,String goodCode,String orderId) throws SQLException {
        log.info("门店:{} ,订单：{}，准备分拣商品：{}",storeId,orderId,goodCode);
        Integer result = null;
        final CallableStatement[] cs={null};

        try{
            result = (Integer) jdbcTemplate.getJdbcOperations().execute(new CallableStatementCreator() {
                public CallableStatement createCallableStatement(Connection conn) throws SQLException {
                    String sql = "{call pkg_prepostion.p_sp_stock(?,?,?,?)}";
                    cs[0] = conn.prepareCall(sql);

                    cs[0].setString(1,storeId);
                    cs[0].setString(2,goodCode);
                    cs[0].setString(3,orderId);
                    cs[0].registerOutParameter(4,OracleTypes.INTEGER);

                    return cs[0];
                }
            }, new CallableStatementCallback<Integer>() {
                @Override
                public Integer doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                    callableStatement.execute();

                    Integer flag = callableStatement.getInt(4);

                    callableStatement.close();
                    return flag;
                }
            });

        }catch (Exception e){
            log.error("调用存储过程失败：{}",ExceptionUtils.getMessage(e));
        }finally {
            cs[0].close();
        }
        return result;
    }


    /**
     * 判断箱码是否重复
     * @param jdbcTemplate
     * @param storeId
     * @param boxCode
     * @return
     */
    public Integer queryBoxCodeIsRepeat(NamedParameterJdbcTemplate jdbcTemplate,String storeId,String boxCode) throws SQLException {
        log.info("封箱前查询箱码是否重复：{}",boxCode);

        Integer result = null;
        final CallableStatement[] cs = {null};

        try{
            result =(Integer) jdbcTemplate.getJdbcOperations().execute(new CallableStatementCreator() {
                public CallableStatement createCallableStatement(Connection conn) throws SQLException {
                    String sql = "{call pkg_prepostion.p_isxmcf(?,?,?)}";
                    cs[0] = conn.prepareCall(sql);
                    cs[0].setString(1,storeId);
                    cs[0].setString(2,boxCode);
                    cs[0].registerOutParameter(3,OracleTypes.INTEGER);

                    return cs[0];
                }
            }, new CallableStatementCallback<Integer>() {
                @Override
                public Integer doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                    callableStatement.execute();

                    Integer flag = callableStatement.getInt(3);

                    callableStatement.close();;
                    return flag;
                }
            });

        }catch (Exception e){
            log.error("调用存储过程失败：{}",ExceptionUtils.getMessage(e));
        }finally {
            cs[0].close();
        }
        return result;
    }

    /**
     * 封箱后，更新数据库表
     * @param jdbcTemplate
     * @param storeId
     * @throws SQLException
     */
    public boolean  goodIsInsert(NamedParameterJdbcTemplate jdbcTemplate,String uuid,String storeId) throws SQLException {
        log.info("封箱后，更新数据库表:{}",uuid);
        final CallableStatement[] cs = {null};
        try {
            jdbcTemplate.getJdbcOperations().execute(
                    new CallableStatementCreator() {
                        public CallableStatement createCallableStatement(Connection con) throws SQLException {
                            String storedProc = "{call Pkg_Prepostion.P_Sorting(?,?)}";
                            cs[0] = con.prepareCall(storedProc);

                            cs[0].setString(1,uuid);
                            cs[0].setString(2, storeId);

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
            log.warn("调用存储过程失败：{}", ExceptionUtils.getMessage(e));
            return false;
        }finally {
            cs[0].close();
        }

        return true;
    }

    public void finishPick(NamedParameterJdbcTemplate jdbcTemplate,String storeId,String orderId,String userId){
        log.info("准备调用存储过程修订单为分拣完成状态.....");
        final CallableStatement[] cs = {null};

        jdbcTemplate.getJdbcOperations().execute(new CallableStatementCreator() {
            public CallableStatement createCallableStatement(Connection conn) throws SQLException {
                String policy = "{call Pkg_Prepostion.p_dd_finish(?, ?,?)}";
                cs[0] = conn.prepareCall(policy);
                cs[0].setString(1, storeId);
                cs[0].setString(2, orderId);
                cs[0].setString(3, userId);

                return cs[0];
            }
        }, new CallableStatementCallback() {
            @Override
            public Object doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                callableStatement.execute();
                callableStatement.close();
                return null;
            }
        });
    }



    public void delBox(NamedParameterJdbcTemplate jdbcTemplate,String storeId,String documentsId,String boxCode) throws Exception {
        log.info("准备调用删除箱码：{}的后台存储过程",boxCode);
        final CallableStatement[] cs = {null};

        try{

            jdbcTemplate.getJdbcOperations().execute(new CallableStatementCreator() {
                public CallableStatement createCallableStatement(Connection con) throws SQLException {
                    String policy = "{call Pkg_Prepostion.p_del_xm(?,?,?)}";
                    cs[0] = con.prepareCall(policy);

                    cs[0].setString(1,storeId);
                    cs[0].setString(2,documentsId);
                    cs[0].setString(3,boxCode);

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
            log.error("调用存储过程删除箱码：{}失败:{}",boxCode,ExceptionUtils.getMessage(e));
            throw new Exception(e);
//            return false;

        }finally {
            cs[0].close();
        }
//        return true;
    }

    public void delGoodInBox(NamedParameterJdbcTemplate jdbcTemplate,String storeId,String documentsId,String setBoxCode,String goodId,String orderId) throws Exception {
        log.error("准备调用删除箱中商品：{}的后台存储过程",setBoxCode+"-"+goodId);
        final CallableStatement[] cs = {null};

        try{
            jdbcTemplate.getJdbcOperations().execute(new CallableStatementCreator() {
                public CallableStatement createCallableStatement(Connection con) throws SQLException {
                    String policy = "{call Pkg_Prepostion.p_del_glbh(?,?,?,?,?)}";
                    cs[0] = con.prepareCall(policy);

                    cs[0].setString(1,storeId);
                    cs[0].setString(2,documentsId);
                    cs[0].setString(3,setBoxCode);
                    cs[0].setString(4,goodId);
                    cs[0].setString(5,orderId);

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
            log.error("调用存储过程删除箱：{}中商品：{}失败:{}",setBoxCode,goodId,ExceptionUtils.getMessage(e));
            throw new Exception(e);
//            return false;
        }finally {
            cs[0].close();
        }
//        return true;
    }

    public void insertTrunkBox(NamedParameterJdbcTemplate jdbcTemplate,String uuid,String storeId) throws Exception {
        log.info("调用存储过程装车......");
        final CallableStatement[] cs = {null};

        try{
            jdbcTemplate.getJdbcOperations().execute(new CallableStatementCreator() {
                public CallableStatement createCallableStatement(Connection con) throws SQLException {
                    String policy = "{call Pkg_Prepostion.p_tranout (?,?)}";
                    cs[0] = con.prepareCall(policy);

                    cs[0].setString(1, uuid);
                    cs[0].setString(2, storeId);

                    return cs[0];
                }
            }, new CallableStatementCallback() {
                @Override
                public Object doInCallableStatement(CallableStatement callableStatement) throws DataAccessException, SQLException {
                    callableStatement.execute();
                    callableStatement.close();

                    return null;
                }
            });

        }catch (Exception e){
            log.error("调用存储过程插入装车箱码失败:{}",ExceptionUtils.getMessage(e));
            throw new Exception(e);
//            return false;
        }finally {
            cs[0].close();
        }
    }

    public void boxInstorage(NamedParameterJdbcTemplate jdbcTemplate,String uuid,String storeId) throws Exception {
        log.info("调出存储过程入库......");
        final CallableStatement[] cs={null};

        try{
            jdbcTemplate.getJdbcOperations().execute(new CallableStatementCreator() {
                public CallableStatement createCallableStatement(Connection con) throws SQLException {
                    String policy = "{call Pkg_Prepostion.p_tranin(?,?)}";
                    cs[0] = con.prepareCall(policy);
                    cs[0].setString(1,uuid);
                    cs[0].setString(2,storeId);

                    return cs[0];
                }
            }, new CallableStatementCallback() {

                @Override
                public Object doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                    callableStatement.execute();
                    callableStatement.close();

                    return  null;
                }
            });
        }catch (Exception e){
            log.error("调用存储过程插入装车箱码失败:{}",ExceptionUtils.getMessage(e));
            throw new Exception(e);
        }finally {
            cs[0].close();
        }
    }

    public void finishPicked(NamedParameterJdbcTemplate jdbcTemplate,String storeId,String orderId,String userId) throws Exception {
        log.info("分拣完成，修改订单状态:{}....",orderId);
        final CallableStatement[] cs = {null};

        try{
            jdbcTemplate.getJdbcOperations().execute(new CallableStatementCreator() {
                public CallableStatement createCallableStatement(Connection conn) throws SQLException {
                    String policy = "{call Pkg_Prepostion.p_dd_finish(?,?,?)}";
                    cs[0] = conn.prepareCall(policy);
                    cs[0].setString(1, storeId);
                    cs[0].setString(2, orderId);
                    cs[0].setString(3, userId);

                    return cs[0];
                }
            }, new CallableStatementCallback() {
                @Override
                public Object doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                    callableStatement.execute();
                    callableStatement.close();

                    return null;
                }
            });

        }catch (Exception e){
            log.error("修改订单状态失败：{}",ExceptionUtils.getMessage(e));
            throw new Exception(e);
        }finally {
            cs[0].close();
        }

    }
}
