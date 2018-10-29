package com.sanjiang.provider.service.impl.closedAllot;

import com.alibaba.dubbo.config.annotation.Service;
import com.sanjiang.constrants.DefaultValue;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.core.ResultCode;
import com.sanjiang.provider.Dao.ClosedAllotDao;
import com.sanjiang.provider.constrants.SqlParam;
import com.sanjiang.provider.model.ClosedAllot.ClosedAllotModel;
import com.sanjiang.provider.model.ClosedAllot.DbckAreaCountModel;
import com.sanjiang.provider.model.ClosedAllot.DbckAreaModel;
import com.sanjiang.provider.model.ClosedAllot.DbckSearch;
import com.sanjiang.provider.model.GoodsBaseModel;
import com.sanjiang.provider.model.Supplier;
import com.sanjiang.provider.service.closedAllot.ClosedAllotService;
import com.sanjiang.provider.util.ConvertExceptionTip;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by byinbo on 2018/7/8.
 */
@Service(
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}",
        version = "1.0.0",
        timeout = 50000
)
@Slf4j
public class ClosedAllotServiceImpl implements ClosedAllotService{

    public static final String LIST_SHOPS_SQL = "select bmbh,bmmc from dm_scbh where bmzt ='3' and bmlx = '44'";

    public static final String GOODS_SELECT_BY_SPBH = "select a.SPMC,a.GLBH,a.SPZT,a.SPJJ from user_xt_spda a , xt_ftm b where a.GLBH=b.GLBH and a.spzt  not in ('5','8','9') and b.SPBH=:as_spbh";

    public static final String GOODS_SELECT_BY_AREA = "select  xh,spmc,spbh,rksl from xt_dbck a,xt_spda b where scbh =:as_scbh and bmbh =:as_bmbh and bh = :bh and a.glbh = b.glbh ";

    public static final String INSERT_DBCK_DATA = "Insert  into xt_dbck (scbh,bmbh,glbh,rksl,rkje,bh,xh,lrrq,zt,spbh) values (:scbh,:bmbh,:glbh,:rksl,:rkje,:bh,:xh,sysdate,'LR',:spbh)";

    public static final String UPDATE_DBCK_DATE = "update xt_dbck set rksl=:rksl,rkje=:rkje,lrrq=sysdate where scbh=:scbh and bmbh=:bmbh and bh=:bh and spbh=:spbh";

    public static final String DELETE_DBCK_BY_XH = "delete xt_dbck where scbh=:as_scbh and bmbh=:as_bmbh and bh=:bh and xh=:xh";

    public static final String DELETE_DBCK_BY_BH = "delete xt_dbck where scbh=:as_scbh and bmbh=:as_bmbh and bh=:bh";

    public static final String LIST_DBCK_BH = "select bh,count(distinct glbh) as types,sum(rksl) as rksl,sum(rkje) as rkje from xt_dbck where scbh =:as_scbh and bmbh =:as_bmbh group by bh";

    public static final String LIST_DBCK_BH_GOODS = "select b.spmc,a.glbh,a.spbh,a.rksl,a.rkje from xt_dbck a, xt_spda b where a.scbh =:as_scbh and a.bmbh =:as_bmbh and bh =:bh and a.glbh = b.glbh order by xh desc";

    @Autowired
    private ClosedAllotDao closedAllotDao;

    @Override
    public ResponseMessage queryShop(String scbh) {
        HikariDataSource hikariDataSource = closedAllotDao.customDataSource(scbh);
        try{
            String listSelect = LIST_SHOPS_SQL;
            if(null != scbh && StringUtils.isNotEmpty(scbh.trim())){
                listSelect = listSelect + "and bmbh ='" + scbh.trim() +"'";
            }
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            List<Supplier> suppliers = namedParameterJdbcTemplate.query(listSelect, (resultSet, i) -> {
                Supplier supplier = new Supplier();
                supplier.setBmbh(resultSet.getString("bmbh"));
                supplier.setBmmc(resultSet.getString("bmmc").trim());
                return supplier;
            });
            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"成功",suppliers);

        }catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "获取对方门店失败！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage queryGoodsBySpbhExcept(String scbh, String spbh) {
        HikariDataSource hikariDataSource = closedAllotDao.customDataSource(scbh);
        try{
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            // 执行存储过程
            closedAllotDao.execScbh(namedParameterJdbcTemplate, scbh);

            Map<String, String> parameterMap = new HashMap<>();
            parameterMap.put(SqlParam.AS_SPBH.value(), spbh);

            List<GoodsBaseModel> goodsBaseModel = namedParameterJdbcTemplate.query(GOODS_SELECT_BY_SPBH, parameterMap, (rs, rowNum) -> {
                GoodsBaseModel goodsBaseModel1 = new GoodsBaseModel();
                goodsBaseModel1.setSpbh(spbh);
                goodsBaseModel1.setGlbh(rs.getString("glbh"));
                goodsBaseModel1.setSpmc(rs.getString("spmc"));
                goodsBaseModel1.setSpzt(rs.getString("spzt"));
                goodsBaseModel1.setSpjj(rs.getDouble("spjj"));
                return goodsBaseModel1;
            });

            if (CollectionUtils.isEmpty(goodsBaseModel)) {
                return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "没有找到商品，或商品处于休眠、删除、淘汰状态", null);
            }
            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"成功",goodsBaseModel);

        }catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "获取商品失败！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage queryGoodsByArea(DbckSearch dbckSearch) {
        String scbh = dbckSearch.getScbh();
        HikariDataSource hikariDataSource = closedAllotDao.customDataSource(scbh);
        try{
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            // 执行存储过程
            closedAllotDao.execScbh(namedParameterJdbcTemplate, scbh);

            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put(SqlParam.AS_SCBH.value(), scbh);
            parameterMap.put(SqlParam.AS_BMBH.value(), dbckSearch.getBmbh());
            parameterMap.put("bh", dbckSearch.getBh());

            List<DbckAreaModel> dbckAreaModel = namedParameterJdbcTemplate.query(GOODS_SELECT_BY_AREA, parameterMap, (rs, rowNum) -> {
                DbckAreaModel dbckAreaModel1 = new DbckAreaModel();
                dbckAreaModel1.setXh(rs.getInt("xh"));
                dbckAreaModel1.setSpbh(rs.getString("spbh"));
                dbckAreaModel1.setRksl(rs.getDouble("rksl"));
                dbckAreaModel1.setSpmc(rs.getString("spmc"));
                return dbckAreaModel1;
            });

            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"成功",dbckAreaModel);

        }catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "获取区域商品列表失败！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage saveDbck(List<ClosedAllotModel> closedAllotModelList) {
        Long startTime = System.currentTimeMillis();
        final String[] scbh = {null};
        final String[] bmbh = {null};
        final Integer[] bh = {null};
        final Integer[] xh = {null};
        if(!CollectionUtils.isEmpty(closedAllotModelList)){
            scbh[0] = closedAllotModelList.get(0).getScbh();
            bmbh[0] = closedAllotModelList.get(0).getBmbh();
            bh[0] = closedAllotModelList.get(0).getBh();
            xh[0] = closedAllotModelList.get(0).getXh();
            if(null == scbh[0] || null == bmbh[0] || null == bh[0] || null == xh[0]){
                return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "门店，对方门店，区域，序号不能为空", null);
            }

        }else {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "调拨出库数据不能为空", null);
        }

        HikariDataSource hikariDataSource = closedAllotDao.customDataSource(scbh[0]);
        try{
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            final Integer[] resultInsert = {null};
            final Integer[] resultUpdate = {null};
            closedAllotModelList.stream().forEach(closedAllotModel -> {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("scbh", closedAllotModel.getScbh());
                paramMap.put("bmbh", closedAllotModel.getBmbh());
                paramMap.put("glbh", closedAllotModel.getGlbh());
                paramMap.put("rksl", closedAllotModel.getRksl());
                paramMap.put("rkje", closedAllotModel.getRkje());
                paramMap.put("bh", closedAllotModel.getBh());
                paramMap.put("xh", closedAllotModel.getXh());
                paramMap.put("spbh", closedAllotModel.getSpbh());

                SqlParameterSource sqlParameterSource = new MapSqlParameterSource(paramMap);
                //  查询该uuid是否存在
                Integer returnResult = namedParameterJdbcTemplate.getJdbcOperations().queryForObject("select count(*) from xt_dbck where scbh = '"+closedAllotModel.getScbh()+"' and bmbh = '"+closedAllotModel.getBmbh()+"'and spbh = '"+closedAllotModel.getSpbh()+"' and bh = "+closedAllotModel.getBh(), Integer.class);
                if(returnResult == 0) {
                    resultInsert[0] = closedAllotDao.operateSQL(namedParameterJdbcTemplate,sqlParameterSource,hikariDataSource,INSERT_DBCK_DATA);
                }else{
                    resultUpdate[0] = closedAllotDao.operateSQL(namedParameterJdbcTemplate,sqlParameterSource,hikariDataSource,UPDATE_DBCK_DATE);
                }
            });

            if(null == resultInsert[0] && null == resultUpdate[0]){
                return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "调拨出库数据保存失败", null);
            }else{
                return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "调拨出库数据成功", null);

            }

        }catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "调拨出库数据保存失败", null);
        } finally {
            Long endTime = System.currentTimeMillis();

            log.info("调拨出库数据保存耗时：{}",(endTime-startTime));
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }

    }

    @Override
    public ResponseMessage deleteDbckByXh(DbckSearch dbckSearch) {
        String scbh = dbckSearch.getScbh();
        HikariDataSource hikariDataSource = closedAllotDao.customDataSource(scbh);
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(SqlParam.AS_SCBH.value(), scbh);
            paramMap.put(SqlParam.AS_BMBH.value(), dbckSearch.getBmbh());
            paramMap.put("bh", dbckSearch.getBh());
            paramMap.put("xh", dbckSearch.getXh());

            SqlParameterSource sqlParameterSource = new MapSqlParameterSource(paramMap);

            Integer result = closedAllotDao.operateSQL(namedParameterJdbcTemplate, sqlParameterSource, hikariDataSource, DELETE_DBCK_BY_XH);
            return (null == result) ?
                    ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "按序号删除调拨出库数据失败！", null) :
                    ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "删除成功！", null);

        } catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "按序号删除调拨出库数据失败！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage listDbcks(String scbh, String bmbh) {
        HikariDataSource hikariDataSource = closedAllotDao.customDataSource(scbh);
        try{
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            Map<String, String> parameterMap = new HashMap<>();
            parameterMap.put(SqlParam.AS_SCBH.value(), scbh);
            parameterMap.put(SqlParam.AS_BMBH.value(), bmbh);

            List<DbckAreaCountModel> dbckAreaCountModels = namedParameterJdbcTemplate.query(LIST_DBCK_BH, parameterMap,(resultSet, i) -> {
                DbckAreaCountModel dbckAreaCountModel = new DbckAreaCountModel();
                dbckAreaCountModel.setBh(resultSet.getInt("bh"));
                dbckAreaCountModel.setTypes(resultSet.getInt("types"));
                dbckAreaCountModel.setRkje(resultSet.getDouble("rkje"));
                dbckAreaCountModel.setRksl(resultSet.getDouble("rksl"));
                return dbckAreaCountModel;
            });
            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"成功",dbckAreaCountModels);

        }catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "获取调拨出库区域明细失败！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage deleteDbckByBh(DbckSearch dbckSearch) {
        String scbh = dbckSearch.getScbh();
        HikariDataSource hikariDataSource = closedAllotDao.customDataSource(scbh);
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(SqlParam.AS_SCBH.value(), scbh);
            paramMap.put(SqlParam.AS_BMBH.value(), dbckSearch.getBmbh());
            paramMap.put("bh", dbckSearch.getBh());

            SqlParameterSource sqlParameterSource = new MapSqlParameterSource(paramMap);

            Integer result = closedAllotDao.operateSQL(namedParameterJdbcTemplate, sqlParameterSource, hikariDataSource, DELETE_DBCK_BY_BH);
            return (null == result) ?
                    ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "按区域删除调拨出库数据失败！", null) :
                    ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "删除成功！", null);

        } catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "按区域删除调拨出库数据失败！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage listBhGoods(DbckSearch dbckSearch) {
        String scbh = dbckSearch.getScbh();
        HikariDataSource hikariDataSource = closedAllotDao.customDataSource(scbh);
        try{
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put(SqlParam.AS_SCBH.value(), scbh);
            parameterMap.put(SqlParam.AS_BMBH.value(), dbckSearch.getBmbh());
            parameterMap.put("bh", dbckSearch.getBh());

            List<DbckAreaModel> dbckAreaModels = namedParameterJdbcTemplate.query(LIST_DBCK_BH_GOODS, parameterMap,(resultSet, i) -> {
                DbckAreaModel dbckAreaModel = new DbckAreaModel();
                dbckAreaModel.setSpmc(resultSet.getString("spmc"));
                dbckAreaModel.setGlbh(resultSet.getString("glbh"));
                dbckAreaModel.setSpbh(resultSet.getString("spbh"));
                dbckAreaModel.setRkje(resultSet.getDouble("rkje"));
                dbckAreaModel.setRksl(resultSet.getDouble("rksl"));
                return dbckAreaModel;
            });
            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"成功",dbckAreaModels);

        }catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "获取调拨出库区域商品明细失败！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage finishedAllot(String scbh, String bmbh) {
        HikariDataSource hikariDataSource = closedAllotDao.customDataSource(scbh);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
        try {
            closedAllotDao.execFinishClosedAllot(namedParameterJdbcTemplate,scbh, bmbh);
        }catch (Exception e){
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), ConvertExceptionTip.convert(ExceptionUtils.getMessage(e)), null);
        }finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
        return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"成功",null);    }
}
