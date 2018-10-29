package com.sanjiang.provider.service.impl.goodsoutoftime;

import com.alibaba.dubbo.config.annotation.Service;
import com.sanjiang.constrants.DefaultValue;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.core.ResultCode;
import com.sanjiang.provider.domain.goodsoutoftime.PreWarnShelfLife;
import com.sanjiang.provider.service.goodsoutoftime.PreWarnShelfLifeService;
import com.sanjiang.provider.util.BaseCustomDao;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.hibernate.HikariConnectionProvider;
import com.zaxxer.hikari.pool.HikariPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by byinbo on 2018/5/11.
 * 保质期预警
 */
@Service(
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}",
        version = "1.0.0",
        timeout = 50000
)
@Slf4j
public class PreWarnShelfLifeServiceImpl implements PreWarnShelfLifeService{

    @Autowired
    private BaseCustomDao baseCustomDao;

    @Override
    public ResponseMessage goodsOutOfTimeCx(String report,
                                                                    String shopId,
                                                                    String jcrq,
                                                                    String fzm,
                                                                    String spbh) {
        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(shopId);
        List<PreWarnShelfLife> preWarnShelfLives = Lists.newArrayList();
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            preWarnShelfLives = baseCustomDao.execGoodsOurOfTimeCx(namedParameterJdbcTemplate, report,shopId,jcrq,fzm,spbh);
        } catch (SQLException e) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"失败", ExceptionUtils.getMessage(e));
        }finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
        return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"成功",preWarnShelfLives);
    }

    /**
     * 生成预警单
     * @param shopId
     * @param fzm
     * @return
     */
    @Override
    public ResponseMessage goodsOutOfTimeCreat(String shopId, String[] fzm) {
        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(shopId);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
        Timestamp jcrq = null;
        try {
            jcrq = baseCustomDao.execGoodsOutOfTime(namedParameterJdbcTemplate, shopId, fzm);
        }catch (Exception e){
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"失败", ExceptionUtils.getMessage(e));
        }finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
        return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"成功",jcrq);

    }

    /**
     * 商品检查
     * @param czlx
     * @param shopId
     * @param glbh
     * @param scrq
     * @param hjbh
     * @param clry
     * @param jcrq
     * @return
     */
    @Override
    public ResponseMessage goodsOutOfTimeCz(String czlx, String shopId, String glbh, String scrq, String hjbh, String clry, String jcrq) {
        log.info("czlx={}"+" shopId={}"+" glbh={}"+" scrq={}"+" hjbh={}"+" clry={}"+" jcrq={}",czlx,shopId,glbh,scrq,hjbh,clry,jcrq);
        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(shopId);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
        try {
            baseCustomDao.execGoodsOutOfTimeCz(namedParameterJdbcTemplate,czlx, shopId, glbh, scrq, hjbh, clry, jcrq);
        }catch (Exception e){
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"失败", ExceptionUtils.getMessage(e));
        }finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
        return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"成功",null);
    }
}
