package com.sanjiang.provider.service.impl.preAllot;

import com.alibaba.dubbo.config.annotation.Service;
import com.sanjiang.constrants.DefaultValue;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.core.ResultCode;
import com.sanjiang.provider.Dao.PreAllotDao;
import com.sanjiang.provider.domain.preAllot.PreTrunkBox;
import com.sanjiang.provider.domain.preAllot.PreTrunkInfo;
import com.sanjiang.provider.model.preAllot.PreBoxModel;
import com.sanjiang.provider.model.preAllot.PreOutGoodsModel;
import com.sanjiang.provider.model.preAllot.PreReceiveOrderModel;
import com.sanjiang.provider.service.preAllot.PreAllotSendAndReviceService;
import com.sanjiang.provider.util.BaseCustomDao;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;
import java.util.*;

@Service(
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}",
        version = "1.0.0",
        timeout = 50000
)
@Slf4j
public class PreAllotSendAndReviceServiceImpl implements PreAllotSendAndReviceService {

    @Autowired
    private BaseCustomDao baseCustomDao;

    @Autowired
    private PreAllotDao preAllotDao;

    private static final String SELECT_TRUNK_INFO = "Select xmh,a.djh,a.BMBH from all_xt_ydb_m a,rz_xm b where a.djh = b.djh  and a.djzt = 'LR'";

    private static final String TRUNK_LOAD = "INSERT into dxsrf(uuid,A1,A2,A3,A4,A5) values (:uuid,:A1,:A2,:A3,:A4,:A5)";

    private static final String SELECT_REVEICE_ORDER = "select djh,ddh,dcscbh,b.bmmc,drscbh,xmh,count(distinct glbh)pzs from union_xt_dbd_xm a,dm_scbh b" +
            " where a.drscbh = :drscbh and " +
            " exists(select 1 from rz_zzck where bmbh = a.drscbh and scbh = a.dcscbh and ckh = a.djh and nvl(sczt,'A')<>'JS') " +
            " and dcscbh = b.bmbh group by djh,dcscbh, b.bmmc,drscbh,xmh,ddh";

    private static final String  SELECT_REVEICE_DETAIL = " select spmc ,(select max(spbh) from xt_ftm where glbh = a.glbh) spbh ,zxsl from union_xt_dbd_xm a,xt_spda b " +
            " where  xmh = :xmh  and a.glbh = b.glbh";

    private static final String INSERT_IN_STORAGE = "Insert into dxsrf(UUID,A1,A2,A3) Values(:uuid,:A1,:A2,:A3) ";

    @Override
    public List<PreBoxModel> getTrunkInfo(String storeId) {
        log.info("门店：{}，查询需装车的订单.....",storeId);

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

        try{
            //执行门店策略
            baseCustomDao.execScbh(jdbcTemplate,storeId);

            List<PreBoxModel> list = jdbcTemplate.query(SELECT_TRUNK_INFO,(resultSet, i) -> {
                PreBoxModel preBoxModel = new PreBoxModel();
                preBoxModel.setBoxCode(resultSet.getString("xmh"));
                preBoxModel.setDocumentsId(resultSet.getString("djh"));
                preBoxModel.setOtherStoreId(resultSet.getString("bmbh"));

                return preBoxModel;

            });

            return list;

        }catch (Exception e) {
            log.error("查询装车订单失败:{}",ExceptionUtils.getMessage(e));
            return null;
        }finally {
            if(!hikariDataSource.isClosed()){
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage trunkLoad(PreTrunkBox preTrunkBox) {
        log.info("扫码后装车......");
        if(preTrunkBox != null ){
            String storeId = preTrunkBox.getStoreId();
            String userId = preTrunkBox.getUserId();
            String uuid = preTrunkBox.getUuid();

            HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            TransactionTemplate template = baseCustomDao.getTransactionTemplate(hikariDataSource);

            HashMap<String,String> paramMap = new HashMap<>();
            paramMap.put("uuid",uuid);
            paramMap.put("A1",storeId);
            paramMap.put("A5",userId);

            List<PreTrunkInfo> list = preTrunkBox.getInfo();
            int size = list.size();
            SqlParameterSource[] sqlParam = new MapSqlParameterSource[size];

            for(int i=0;i<size;i++) {
                PreTrunkInfo trunkInfo = list.get(i);

                paramMap.put("A2", trunkInfo.getBoxCode());
                paramMap.put("A3", trunkInfo.getDocumentsId());
                paramMap.put("A4", trunkInfo.getOtherStoreId());

                sqlParam[i] = new MapSqlParameterSource(paramMap);
            }

            boolean result = template.execute(transactionStatus -> {

                try{
                    int[] num = jdbcTemplate.batchUpdate(TRUNK_LOAD,sqlParam);
                    if(null == num ||num.length != size) {
                        log.error("装车失败......");
                        return false;
                    }
                    preAllotDao.insertTrunkBox(jdbcTemplate,uuid,storeId);
                    return true;

                } catch (SQLException e) {
                    transactionStatus.setRollbackOnly();
                    log.error("装车异常:{}", ExceptionUtils.getMessage(e));
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

            });

            if(result){
                return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"装车成功",result);
            }else{
                return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"装车失败",null);
            }

        }else{
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"装车参数错误",null);
        }
    }

    @Override
    public List<PreReceiveOrderModel> getReceviceOrderInfo(String storeId) {
        log.info("查询入库订单列表信息:{}",storeId);

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

        try {
            //执行门店策略
            baseCustomDao.execScbh(jdbcTemplate,storeId);

            HashMap<String,String> param = new HashMap<>();
            param.put("drscbh",storeId);


            List<PreReceiveOrderModel> list = jdbcTemplate.query(SELECT_REVEICE_ORDER,param,(resultSet, i) -> {
                PreReceiveOrderModel preReceiveOrderModel = new PreReceiveOrderModel();
                preReceiveOrderModel.setDocumentsId(resultSet.getString("djh"));         //单据号
                preReceiveOrderModel.setOrderId(resultSet.getString("ddh"));             //订单号
                preReceiveOrderModel.setOtherStoreID(resultSet.getString("dcscbh"));     //调出门店ID
                preReceiveOrderModel.setOtherStoreName(resultSet.getString("bmmc"));     //调出门店名
                preReceiveOrderModel.setStoreId(resultSet.getString("drscbh"));          //调入门店编号，本店编号
                preReceiveOrderModel.setBoxCode(resultSet.getString("xmh"));            //箱码号
                preReceiveOrderModel.setKindCount(resultSet.getInt("pzs"));             //品种数

                return preReceiveOrderModel;
            });

            return list;

        } catch (Exception e) {
            log.error("查询入库订单失败:{}",ExceptionUtils.getMessage(e));
            return null;
        }finally {
            if(!hikariDataSource.isClosed()){
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public List<PreOutGoodsModel> getReceiveBoxGoodInfo(String storeId, String otherStoreId, String documentsId, String boxCode) {
        log.info("查询入库订单箱码详情：{}",documentsId);

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

        try {
            //执行门店策略
            baseCustomDao.execScbh(jdbcTemplate,storeId);

            HashMap<String,String> param = new HashMap<>();
            param.put("xmh",boxCode);

            List<PreOutGoodsModel> list = jdbcTemplate.query(SELECT_REVEICE_DETAIL,param,(resultSet, i) -> {
                PreOutGoodsModel good = new PreOutGoodsModel();
                good.setGoodName(resultSet.getString("spmc"));  //商品名称
                good.setGoodCode(resultSet.getString("spbh"));  //商品编号（条码）
                good.setPickedCount(resultSet.getInt("zxsl"));  //数量

                return good;
            });
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询入库订单:{}箱码：{}详情失败：{}",documentsId,boxCode,ExceptionUtils.getMessage(e));
            return null;
        }finally {
            if(!hikariDataSource.isClosed()){
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage scanBoxInStorage(String uuid,String storeId, String boxCode,String userId) {
        log.info("扫描箱码入库:{}",boxCode);

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
        TransactionTemplate template = baseCustomDao.getTransactionTemplate(hikariDataSource);

        HashMap<String,String> insert_param = new HashMap<>();
        insert_param.put("uuid",uuid);
        insert_param.put("A1",storeId);
        insert_param.put("A2",boxCode);
        insert_param.put("A3",userId);

        HashMap<String,String> select_param = new HashMap<>();
        select_param.put("xmh",boxCode);

        try {
            //执行门店策略
            baseCustomDao.execScbh(jdbcTemplate,storeId);

            List<PreOutGoodsModel> list = jdbcTemplate.query(SELECT_REVEICE_DETAIL,select_param,(resultSet, i) ->{
                PreOutGoodsModel good = new PreOutGoodsModel();
                good.setGoodName(resultSet.getString("spmc"));  //商品名称
                good.setGoodCode(resultSet.getString("spbh"));  //商品编号（条码）
                good.setPickedCount(resultSet.getInt("zxsl"));  //数量

                return good;
            });

            boolean flag = template.execute(transactionStatus -> {

                try {
                    //插入数据库
                    Integer num = jdbcTemplate.update(INSERT_IN_STORAGE,insert_param);
                    if(null ==num){
                        log.error("箱码：{}入库失败",boxCode);
                        return false;
                    }
                    //调用村粗过程更新数据库
                    preAllotDao.boxInstorage(jdbcTemplate,uuid,storeId);

                    return true;

                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                    log.error("箱码：{}入库失败：{}",boxCode,ExceptionUtils.getMessage(e));
                    return false;
                }
            });

            if(flag){
                if(null != list){
                    return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"入库成功",list);
                }else{
                    return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"入库成功,但查询箱中商品失败",null);
                }
            }else{
                return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"入库失败",list);
            }

        } catch (Exception e) {
            log.error("箱码：{}入库失败：{}",boxCode,ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"入库失败",null);
        }finally {
            if(!hikariDataSource.isClosed()){
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }
}
