package com.sanjiang.provider.service.impl.NoOrderCollect;

import com.alibaba.dubbo.config.annotation.Service;
import com.sanjiang.constrants.DefaultValue;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.core.ResultCode;
import com.sanjiang.provider.Dao.NoOrderCollectDao;
import com.sanjiang.provider.constrants.SqlParam;
import com.sanjiang.provider.model.NoOrderCollectGoods;
import com.sanjiang.provider.model.ProductInfo;
import com.sanjiang.provider.model.Supplier;
import com.sanjiang.provider.service.noOrderCollect.NoOrderCollectService;
import com.sanjiang.provider.util.ConvertExceptionTip;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 无订单收货
 * Created by byinbo on 2018/6/25.
 */
@Service(
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}",
        version = "1.0.0",
        timeout = 50000
)
@Slf4j
public class NoOrderCollectServiceImpl implements NoOrderCollectService{

    private static final String LIST_SUPPLIERS_SELECT = "select bmbh,bmmc from dm_bmbh WHERE bmlx = '66' and NVL(DDfs,'E')='E'";

    private static final String PRODUCT_SELECT_BY_SPBH = "select f.spbh,x.glbh,x.spmc,x.dhgg,x.spjj from user_xt_spda x,xt_ftm f where x.glbh=f.glbh and  f.spbh=:as_spbh";

    private static final String INSERT_NO_ORDER_DATA = "insert into dxsrf (UUID,a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,scrq,a12,a13) values (:uuid,:a1,:a2,:a3,:a4,:a5,:a6,:a7,:a8,:a9,:a10,:scrq,:a12,:a13)";

    @Autowired
    private NoOrderCollectDao noOrderCollectDao;

    @Override
    public ResponseMessage getSuppliers(String scbh, String bmbh) {
        HikariDataSource hikariDataSource = noOrderCollectDao.customDataSource(scbh);
        try{
            String listSelect = LIST_SUPPLIERS_SELECT;
            if(null != bmbh && StringUtils.isNotEmpty(bmbh.trim())){
                listSelect = listSelect + "and bmbh ='" + bmbh.trim() +"'";
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
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "获取供应商失败！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage getProductBySpbh(String scbh,String spbh) {
        HikariDataSource hikariDataSource = noOrderCollectDao.customDataSource(scbh);
        try{
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            // 执行存储过程
            noOrderCollectDao.execScbh(namedParameterJdbcTemplate, scbh);
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(SqlParam.AS_SPBH.value(), spbh);
            ProductInfo product = namedParameterJdbcTemplate.queryForObject(PRODUCT_SELECT_BY_SPBH, paramMap, (RowMapper<ProductInfo>) (resultSet, i) -> {
                ProductInfo productInfo = new ProductInfo();
                productInfo.setGg(resultSet.getDouble("dhgg"));
                productInfo.setGlbh(resultSet.getString("glbh"));
                productInfo.setSpbh(resultSet.getString("spbh"));
                productInfo.setSpjj(resultSet.getDouble("spjj"));
                productInfo.setSpmc(resultSet.getString("spmc"));
                return productInfo;
            });
            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "成功", product);

        }catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "获取商品信息失败！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }

    }

    @Override
    public ResponseMessage save(List<NoOrderCollectGoods> noOrderCollectGoodsList) {
        Long startTime = System.currentTimeMillis();
        String scbh = null;
        String uuid = null;
        if(!CollectionUtils.isEmpty(noOrderCollectGoodsList)){
            scbh = noOrderCollectGoodsList.get(0).getA2();
            uuid = noOrderCollectGoodsList.get(0).getUuid();
            if(null == uuid){
                return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "uuid不能为空", null);
            }
        }else {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "无订单收货数据不能为空", null);
        }
        HikariDataSource hikariDataSource = noOrderCollectDao.customDataSource(scbh);
        try{
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            TransactionTemplate transactionTemplate = noOrderCollectDao.getTransactionTemplate(hikariDataSource);

            int[] result = transactionTemplate.execute(status -> {
                try {
                    return namedParameterJdbcTemplate.batchUpdate(INSERT_NO_ORDER_DATA, SqlParameterSourceUtils.createBatch(noOrderCollectGoodsList.toArray()));
                } catch (Exception ex) {
                    status.setRollbackOnly();
                    log.warn("收货数据上传异常:{}", ExceptionUtils.getMessage(ex));
                    return null;
                }
            });

            if(null == result){
                return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "无订单收货数据保存失败", null);
            }else{
                //调用存储过程
                try {
                    noOrderCollectDao.execNoOrderCollect(namedParameterJdbcTemplate,uuid,scbh);
                }catch (Exception e){
                    log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(e));
                    return ResponseMessage.responseMessage(ResultCode.FAIL.code(), ConvertExceptionTip.convert(ExceptionUtils.getMessage(e)), null);
                }

                return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "无订单收货成功", null);

            }

        }catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "无订单收货数据保存失败", null);
        } finally {
            Long endTime = System.currentTimeMillis();

            log.info("无订单收货数据保存耗时：{}",(endTime-startTime));
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }

    }
}
