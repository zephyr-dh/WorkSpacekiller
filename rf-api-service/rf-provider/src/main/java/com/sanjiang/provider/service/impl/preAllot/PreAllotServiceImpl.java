package com.sanjiang.provider.service.impl.preAllot;

import com.alibaba.dubbo.config.annotation.Service;
import com.sanjiang.constrants.DefaultValue;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.core.ResultCode;
import com.sanjiang.provider.Dao.PreAllotDao;
import com.sanjiang.provider.domain.preAllot.PreOrderGood;
import com.sanjiang.provider.model.Supplier;
import com.sanjiang.provider.model.preAllot.PreScanGoodModel;
import com.sanjiang.provider.model.preAllot.PreOrdersModel;
import com.sanjiang.provider.service.preAllot.PreAllotService;
import com.sanjiang.provider.util.BaseCustomDao;
import com.sanjiang.provider.util.DateUtil;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.ResultSet;
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
public class PreAllotServiceImpl implements PreAllotService {

    @Autowired
    private BaseCustomDao baseCustomDao;

    @Autowired
    private PreAllotDao preAllotDao;

    private static final String SHOP_NUMBER_AND_NAME = "SELECT bmbh,bmmc from dm_scbh where bmbh=(SELECT value from xt_csk where section='XT' and name='PREPOSITION')";

    private static final String SHOP_OLD_ORDERS = "SELECT djh,to_char(zdsj,'yyyymmdd') zdsj,b.spmc,(select spbh from xt_ftm where glbh = a.glbh and rownum = 1)spbh,shsl from " +
                                                  "user_xt_dbd_dh a,xt_spda b where zdsj>trunc(sysdate-7) and a.glbh = b.glbh order by zdsj desc";

    private static final String SELECT_MAX_DJH = "SELECT nvl(max(djh),0)+1 djh from xt_dbd_m";

    private static final String INSERT_PRE_ORDER = "INSERT into xt_dbd_m(djh,scbh,dfscbh,dblx,zt,zdr,zdsj,scr,scsj,yxsj) values (:djh,:scbh,:dfscbh,'DHSQ','SH',:zdr,:zdsj,:scr,:scsj,:yxsj)";

    private static final String INSERT_PRE_GOOD = "INSERT into xt_dbd_d(djh,scbh,dblx,glbh,sqsl,shsl) values (:djh,:scbh,'DHSQ',:glbh,:sqsl,:shsl)";

    @Override
    public ResponseMessage getOrderStore(String storeId) {
        log.info("前置仓订货门店：{}",storeId);

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

        try {
            //执行门店策略
            baseCustomDao.execScbh(jdbcTemplate,storeId);

            //查询门店编号和名称
            Supplier supplier = jdbcTemplate.queryForObject(SHOP_NUMBER_AND_NAME,new HashMap<>(),(resultSet, i) -> {
                Supplier supplier1 = new Supplier();
                supplier1.setBmbh(resultSet.getString("bmbh"));
                supplier1.setBmmc(resultSet.getString("bmmc"));

                return supplier1;
            });
            String msg = supplier.getBmbh()+"-"+supplier.getBmmc();
            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"查询对方门店成功",msg);

        } catch (EmptyResultDataAccessException e){
            log.error("无可订货门店:{}",e);
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"无可订货门店",null);
        } catch (SQLException e) {
            log.error("订货门店查询异常：{}",ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"订货门店查询异常",null);
        }finally {
            if(!hikariDataSource.isClosed()){
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public List<PreScanGoodModel> getPreGood(String storeId, String otherStoreId, String goodCode) {
        log.info("查询订货商品：{}",goodCode);
        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

        try {
            //执行存储过程，返回对应商品
            List goodList = preAllotDao.execOrderGood(jdbcTemplate,storeId,otherStoreId,goodCode);

            if(null == goodList){
                return null;
            }

            List<PreScanGoodModel> result = new ArrayList<>();
            for (Object o : goodList) {
                Map rowMap = (Map) o;
                PreScanGoodModel good = new PreScanGoodModel();
                good.setGoodId(rowMap.get("glbh").toString());           //商品管理码
                good.setGoodCode(rowMap.get("spbh").toString());         //商品条码
                good.setGoodName(rowMap.get("spmc").toString());         //商品名称
                good.setOrderSpecification(rowMap.get("dhgg").toString());                 //订货规格
                good.setRepertoryCount(Integer.valueOf(rowMap.get("kcsl").toString()));    //库存数量
                good.setGoodStatus(rowMap.get("spztmc").toString());                       //商品状态
                good.setNumInWeek(Integer.valueOf(rowMap.get("xssl").toString()));         //周销
                good.setNumInMonth(Integer.valueOf(rowMap.get("xl28").toString()));        //四周均销
                good.setGoodType("0".equals(rowMap.get("splx").toString())?"称重码":"标品");   //商品类型
                good.setOrderCountNow(Integer.valueOf(rowMap.get("dhsl").toString()));         //已订数量
                good.setOtherShopRepertoryCount(Integer.valueOf(rowMap.get("kcsl_df").toString()));   //对方门店商品库存

                result.add(good);
            }
            return  result;
        } catch (SQLException e) {
            log.error("查询订货商品异常：{}",ExceptionUtils.getMessage(e));
            return null;
        }finally {
            if(!hikariDataSource.isClosed()){
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public List<PreOrdersModel> getPreOldOrders(String storeId) {
        log.info("前置仓历史订单:{}",storeId);

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

        try {
            //执行门店策略
            baseCustomDao.execScbh(jdbcTemplate,storeId);



//            jdbcTemplate.query(SHOP_OLD_ORDERS, new RowMapper<PreOrdersModel>() {
//                @Override
//                public PreOrdersModel mapRow(ResultSet resultSet, int i) {
//
//                    return null;
//                }
//            });
//


            //查询历史订单
            List<PreOrdersModel> list = jdbcTemplate.query(SHOP_OLD_ORDERS, (resultSet, i) -> {
                PreOrdersModel preOrdersModel = new PreOrdersModel();
                preOrdersModel.setOrderId(resultSet.getString("djh"));     //订单编号
                preOrdersModel.setOrderDate(resultSet.getString("zdsj"));  //订单日期
                preOrdersModel.setGoodId(resultSet.getString("spbh"));     //商品编号
                preOrdersModel.setGoodName(resultSet.getString("spmc"));   //商品名称
                preOrdersModel.setCount(resultSet.getInt("shsl"));         //商品数量
                return preOrdersModel;

            });
            return list;

        } catch (SQLException e) {
            log.error("查询历史订单异常：{}",ExceptionUtils.getMessage(e));
            return null;
        }finally {
            if(!hikariDataSource.isClosed()){
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }

    }

    @Override
    public ResponseMessage insertOrder(String storeId, String otherStoreId, String userId, List<PreOrderGood> goodList) {
        log.info("开始保存订货订单，本地商场编号：{}，对方商场编号：{}，操作员：{}",storeId,otherStoreId,userId);

        if(StringUtils.isEmpty(storeId) || StringUtils.isEmpty(otherStoreId) || null == goodList || goodList.size() <=0){
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"保存订货订单参数错误",null);
        }

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

        try {
            //执行门店策略
            baseCustomDao.execScbh(jdbcTemplate,storeId);

            //查询得到目前的单据号
            Integer djh = jdbcTemplate.queryForObject(SELECT_MAX_DJH,new HashMap<>(),Integer.class);

            HashMap orderParam = new HashMap();
            Date date = new Date();
            orderParam.put("djh",djh);
            orderParam.put("scbh",storeId);
            orderParam.put("dfscbh",otherStoreId);
            orderParam.put("zdr",userId);
            orderParam.put("zdsj",new Date());
            orderParam.put("scr",userId);
            orderParam.put("scsj",date);
            orderParam.put("yxsj",DateUtil.addDate(date,"yyyy-MM-dd HH:mm:ss",1));


            //批量插入商品的参数信息
            int size = goodList.size();
            SqlParameterSource[] sqlParameterSources = new MapSqlParameterSource[size];
            for(int i=0;i<size;i++){
                PreOrderGood good = goodList.get(i);
                HashMap goodParam = new HashMap();
                goodParam.put("djh",djh);
                goodParam.put("scbh",storeId);
                goodParam.put("glbh",good.getGoodId());
                goodParam.put("sqsl",good.getCount());
                goodParam.put("shsl",good.getCount());

                sqlParameterSources[i] = new MapSqlParameterSource(goodParam);
            }

            //使用事物模板，将插入订单和商品放在一个订单里面，保证事物的一致性
            TransactionTemplate transactionTemplate = baseCustomDao.getTransactionTemplate(hikariDataSource);
            boolean result = transactionTemplate.execute(status -> {
                try{
                    //插入订单信息
                    Integer orderResult = jdbcTemplate.update(INSERT_PRE_ORDER, new MapSqlParameterSource(orderParam));

                    //批量插入商品
                    int[] goodResult = jdbcTemplate.batchUpdate(INSERT_PRE_GOOD,sqlParameterSources);

                    if(orderResult == 1 && goodResult != null &&goodResult.length == size){
                        return true;
                    }else{
                        status.setRollbackOnly();
                        log.error("保存订单异常,返回结果和插入数量不对");
                        log.error("实际订单数：{}，实际商品数：{}",1,size);
                        log.error("返回插入订单数：{}，商品数：{}",orderResult,goodResult == null ? null : goodResult.length);
                        return false;
                    }

                }catch (Exception e){
                    status.setRollbackOnly();
                    log.error("保存订单异常:{}", ExceptionUtils.getMessage(e));
                    return false;
                }
            });

            if(result){
                return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"保存订单成功",null);
            }else{
                return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"保存订单失败",null);
            }

        } catch (EmptyResultDataAccessException e){
            log.error("保存订单异常,无法生成单据号：{}",ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"保存订单异常，无法生成单据号",null);
        } catch (SQLException e) {
            log.error("保存订单异常：{}",ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"保存订单异常",null);
        }finally {
            if(!hikariDataSource.isClosed()){
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

}
