package com.sanjiang.provider.service.impl.collectGoods;

import com.alibaba.dubbo.config.annotation.Service;
import com.sanjiang.constrants.DefaultValue;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.core.ResultCode;
import com.sanjiang.provider.Dao.PreCheckListDao;
import com.sanjiang.provider.domain.collectGoods.GoodsDetail;
import com.sanjiang.provider.domain.collectGoods.PreCheckList;
import com.sanjiang.provider.model.CollectGoodsModel;
import com.sanjiang.provider.model.PreCheckModel;
import com.sanjiang.provider.service.collectGoods.PreCheckListService;
import com.sanjiang.provider.util.ConvertExceptionTip;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by byinbo on 2018/5/21.
 *
 */
@Service(
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}",
        version = "1.0.0",
        timeout = 50000
)
@Slf4j
public class PreCheckListServiceImpl implements PreCheckListService{

    @Autowired
    private PreCheckListDao preCheckListDao;

    private static final String COLLECT_GOODS_INSERT = "insert into dxsrf (uuid,xgrq,a1,a2,a3,a4,a5) values (:uuid,sysdate,:djdh,:scbh,:bmbh,:glbh,:sssl)";

    private static final String INSERT_PRE_CHECK = "insert into dxsrf(uuid,xgrq,a1,a2,a3,a4)  values (:uuid,sysdate,:scbh,:bmbh,:cch,:clry)";


    @Override
    public ResponseMessage<Object> getReportPreCheckList(String scbh) {
        HikariDataSource hikariDataSource = preCheckListDao.customDataSource(scbh);
        List<PreCheckList> preCheckLists = Lists.newArrayList();
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            preCheckLists = preCheckListDao.execReportPreCheckList(namedParameterJdbcTemplate, scbh);
        }catch (Exception e){
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), ConvertExceptionTip.convert(ExceptionUtils.getMessage(e)), null);
        }finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }

        return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"成功",preCheckLists);
    }

    @Override
    public ResponseMessage<Object> queryPreCheckList(String scbh, String bmbh, String cch) {
        HikariDataSource hikariDataSource = preCheckListDao.customDataSource(scbh);
        List<PreCheckList> preCheckLists = Lists.newArrayList();
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            preCheckLists = preCheckListDao.execQueryPreCheckList(namedParameterJdbcTemplate, scbh, bmbh, cch);
        }catch (Exception e){
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), ConvertExceptionTip.convert(ExceptionUtils.getMessage(e)),
                    null);
        }finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }

        return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"成功",preCheckLists);
    }

    @Override
    public ResponseMessage createPreCheckList(String scbh, String uuid) {

        HikariDataSource hikariDataSource = preCheckListDao.customDataSource(scbh);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
        Integer djdh = null;
        try {
            djdh = preCheckListDao.execCreatePreCheckList(namedParameterJdbcTemplate,scbh, uuid);
        }catch (Exception e){
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), ConvertExceptionTip.convert(ExceptionUtils.getMessage(e)), null);
        }finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
        return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"成功",djdh);
    }

    @Override
    public ResponseMessage inspectGoodsList(String scbh, String djdh) {

        HikariDataSource hikariDataSource = preCheckListDao.customDataSource(scbh);
        List<GoodsDetail> GoodsDetails = Lists.newArrayList();
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            GoodsDetails = preCheckListDao.execInspectGoodsList(namedParameterJdbcTemplate, scbh, djdh);
        }catch (Exception e){
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), ConvertExceptionTip.convert(ExceptionUtils.getMessage(e)),
                    null);
        }finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }

        return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"成功",GoodsDetails);
    }

    @Override
    public ResponseMessage verifyPreCheckList(String scbh, String djdh) {

        HikariDataSource hikariDataSource = preCheckListDao.customDataSource(scbh);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
        try {
            preCheckListDao.execVerifyPreCheckList(namedParameterJdbcTemplate,scbh, djdh);
        }catch (Exception e){
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), ConvertExceptionTip.convert(ExceptionUtils.getMessage(e)), null);
        }finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
        return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"成功",null);

    }

    @Override
    public ResponseMessage uploadData(List<CollectGoodsModel> collectGoodsModels) {
        Long startTime = System.currentTimeMillis();
        if(CollectionUtils.isEmpty(collectGoodsModels)){
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"收货数据为空", null);
        }
        String shopId = collectGoodsModels.get(0).getScbh();
        String djdh = collectGoodsModels.get(0).getDjdh();

        HikariDataSource hikariDataSource = preCheckListDao.customDataSource(shopId);
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            TransactionTemplate transactionTemplate = preCheckListDao.getTransactionTemplate(hikariDataSource);

            int[] result = transactionTemplate.execute(status -> {
                try {
                    return namedParameterJdbcTemplate.batchUpdate(COLLECT_GOODS_INSERT, SqlParameterSourceUtils.createBatch(collectGoodsModels.toArray()));
                } catch (Exception ex) {
                    status.setRollbackOnly();
                    log.warn("收货数据上传异常:{}", ExceptionUtils.getMessage(ex));
                    return null;
                }
            });

            if(null == result){
                return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "收货数据保存失败", null);
            }else{
                //调用审核
                ResponseMessage responseMessage = verifyPreCheckList(shopId, djdh);

                if(responseMessage.getCode() == ResultCode.FAIL.code()){
                    log.info("审核失败：{}", responseMessage.getMessage());
                    return ResponseMessage.responseMessage(ResultCode.FAIL.code(), responseMessage.getMessage(), null);
                }else{
                    return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "审核成功", null);

                }
            }
        } catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "审核失败", null);
        } finally {
            Long endTime = System.currentTimeMillis();

            log.info("审核耗时：{}",(endTime-startTime));
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage insertPreCheckData(List<PreCheckModel> preCheckModel) {
        if(CollectionUtils.isEmpty(preCheckModel)){
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"上传预检单数据为空", null);
        }
        String shopId = preCheckModel.get(0).getScbh();
        String as_uuid = preCheckModel.get(0).getUuid();

        HikariDataSource hikariDataSource = preCheckListDao.customDataSource(shopId);
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

            //  查询该uuid是否存在
            Integer returnResult = namedParameterJdbcTemplate.getJdbcOperations().queryForObject("select count(*) from dxsrf where uuid = '"+as_uuid+"'", Integer.class);

            if(returnResult == 0){
                //插入预检单数据

                TransactionTemplate transactionTemplate = preCheckListDao.getTransactionTemplate(hikariDataSource);
                int[] result = transactionTemplate.execute(status -> {
                    try {
                        return namedParameterJdbcTemplate.batchUpdate(INSERT_PRE_CHECK, SqlParameterSourceUtils.createBatch(preCheckModel.toArray()));
                    } catch (Exception ex) {
                        status.setRollbackOnly();
                        log.warn("收货数据上传异常:{}", ExceptionUtils.getMessage(ex));
                        return null;
                    }
                });

                if(null == result){
                    return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "预检单保存异常", null);
                }else{
                    //调用生成预检单
                    ResponseMessage responseMessage = createPreCheckList(shopId, as_uuid);
                    if(responseMessage.getCode() == ResultCode.SUCCESS.code()){
                        String djdh = String.valueOf(responseMessage.getData());
                        //调用验货明细接口
                        ResponseMessage responseMessage1 = inspectGoodsList(shopId, djdh);
                        if(responseMessage1.getCode() == ResultCode.SUCCESS.code()){
                            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "成功", responseMessage1.getData());
                        }else{
                            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), responseMessage1.getMessage(), null);
                        }
                    }else{
                        return ResponseMessage.responseMessage(ResultCode.FAIL.code(), responseMessage.getMessage(), null);
                    }
                }
            }else{
                return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "uuid已存在", null);
            }

        }catch (Exception e){
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "失败", null);
        }finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }
}
