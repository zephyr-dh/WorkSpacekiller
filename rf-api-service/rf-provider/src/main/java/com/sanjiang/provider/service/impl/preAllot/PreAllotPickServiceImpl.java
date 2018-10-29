package com.sanjiang.provider.service.impl.preAllot;

import com.alibaba.dubbo.config.annotation.Service;
import com.sanjiang.constrants.DefaultValue;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.core.ResultCode;
import com.sanjiang.provider.Dao.PreAllotDao;
import com.sanjiang.provider.domain.preAllot.PreOrderGood;
import com.sanjiang.provider.domain.preAllot.PreSealBox;
import com.sanjiang.provider.model.preAllot.PreOrdersModel;
import com.sanjiang.provider.model.preAllot.PreOutGoodsModel;
import com.sanjiang.provider.model.preAllot.PreBoxModel;
import com.sanjiang.provider.service.preAllot.PreAllotPickService;
import com.sanjiang.provider.util.BaseCustomDao;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class PreAllotPickServiceImpl implements PreAllotPickService {

    @Autowired
    private BaseCustomDao baseCustomDao;

    @Autowired
    private PreAllotDao preAllotDao;

    private static final String INSERT_BOX_CODE = "INSERT into dxsrf(uuid,xgrq,A1,A2,A3,A4,A5,A6,A7) values(:uuid,:sysdate,:A1,:A2,:A3,:A4,:A5,:A6,:A7)";

    private static final String SELECT_BOX_INFO = "select m.djh, d.xmh, count(distinct d.glbh) || '/' || sum(d.zxsl) goodCount,lx, c.BMmc " +
            "  from all_xt_ydb_m m, xt_dbd_xm d, dm_scbh c where m.djh = d.djh and m.scbh = d.dcscbh " +
            "  and m.djzt = 'LR' and m.bmbh = c.bmbh and d.ddh =:ddh group by m.djh, d.xmh, c.BMBH, c.BMmc, lx ";

    private static final String GOOD_INBOX_INFO = "select spmc,a.glbh,a.xmh,a.ddh,(select max(spbh) from xt_ftm where glbh = a.glbh) spbh," +
            "a. ZXSL from xt_dbd_xm a,user_xt_spda b where djh = :djh and xmh = :xmh and a.glbh = b.glbh";


    @Override
    public List<PreOrdersModel> getPreOrder(String storeId) {
        log.info("查询有效的调出订单：{}",storeId);

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

        try {
            //执行存储过程
            List orderList = preAllotDao.getValidOrder(jdbcTemplate,storeId);

            if(null == orderList){
                return null;
            }

            List<PreOrdersModel> list = new ArrayList<>();
            for(Object o: orderList){
                Map rowMap = (Map) o;

                PreOrdersModel order = new PreOrdersModel();
                order.setOrderType(rowMap.get("lx").toString());         //订单类型
                order.setOtherStoreID(rowMap.get("scbh").toString());    //对方门店编号
                order.setOtherStoreName(rowMap.get("bmjc").toString());  //对方门店简称
                order.setOrderDate(rowMap.get("zdrq").toString());       //订单日期
                order.setOrderId(rowMap.get("djh").toString());          //单据号
                order.setKindCount(Integer.valueOf(rowMap.get("cnt").toString()));  //种类数量
                order.setOrderStatus(rowMap.get("zt").toString());       //订单状态

                list.add(order);
            }
            return list;

        } catch (Exception e) {
            log.error("查询调出订单时异常：{}",ExceptionUtils.getMessage(e));
            return null;
        }finally {
            if(!hikariDataSource.isClosed()){
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public List<PreOutGoodsModel> getPreGoodMsg(String storeId, String orderId) {
        log.info("查询调出订单：{}的商品信息",orderId);

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

        try {

            List goodList = preAllotDao.getPreOutGoods(jdbcTemplate,storeId,orderId);

            if(null == goodList){
                return null;
            }
            List<PreOutGoodsModel> list = new ArrayList<>();
            for(Object o: goodList){
                Map rowMap = (Map) o;

                PreOutGoodsModel good = new PreOutGoodsModel();
                good.setGoodId(rowMap.get("glbh").toString());        //管理码
                good.setOrderId(rowMap.get("djh").toString());        //订单号
                good.setGoodName(rowMap.get("spmc").toString());      //商品名称
                good.setGoodCode(rowMap.get("spbh").toString());      //商品条码
                good.setGoodSelfId(rowMap.get("hjbh").toString());    //货架编号
                good.setPickCount(Integer.valueOf(rowMap.get("shsl").toString()));   //待拣数量
                good.setPickedCount(Integer.valueOf(rowMap.get("sdsl").toString())); //已拣数量

                list.add(good);
            }
            return list;

        } catch (SQLException e) {
            log.error("查询调出订单商品时异常：{}",ExceptionUtils.getMessage(e));
            return  null;
        }finally {
            if(!hikariDataSource.isClosed()){
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage scanGoodCode(String storeId, String goodCode,String orderId) {
        log.info("扫描商品条码，进行分拣：{}",goodCode);

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

        try {
            //0 正常可以调拨    1   无此商品   2  淘鲜达无库存  3 非淘鲜达无库存
            Integer result =preAllotDao.queryGoodIsAllot(jdbcTemplate,storeId,goodCode,orderId);

            if(null == result){
                return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"扫描商品条码失败",null);
            }else{
                return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"扫描商品条码成功",result);
            }

        } catch (SQLException e) {
            log.error("扫描商品条码失败:{}",e);
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"扫描商品条码失败",null);
        }finally {
            if(!hikariDataSource.isClosed()){
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }

    }

    @Override
    public ResponseMessage insertBoxGood(PreSealBox preSealBox) {
        String uuid = preSealBox.getUuid();
        String storeId = preSealBox.getStoreId();
        String otherStoreId = preSealBox.getOtherStoreId();
        String boxCode = preSealBox.getBoxCode();
        List<PreOrderGood> goodList = preSealBox.getGoodList();

        log.info("扫描箱码，装备装箱：{}",boxCode);

        if(StringUtils.isEmpty(storeId) || StringUtils.isEmpty(otherStoreId) ||StringUtils.isEmpty(boxCode)|| null == goodList || goodList.size() <=0){
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"装箱参数错误",null);
        }

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

        try {
            log.info("判断箱码是否重复：{}",boxCode);
            // 0 正常    1  重复；
            Integer result = preAllotDao.queryBoxCodeIsRepeat(jdbcTemplate,storeId,boxCode);

            if(null == result){
                return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"判断箱码重复失败",null);
            }else if(result == 1){
                log.info("箱码重复：{}",boxCode);
                return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"箱码重复",result);
            }

            log.info("箱码未重复，开始插入箱码信息：{}",boxCode);

            //执行门店策略
            baseCustomDao.execScbh(jdbcTemplate,storeId);

            //sql参数
            HashMap boxParam = new HashMap();
            boxParam.put("uuid",uuid);
            boxParam.put("sysdate",new Date());
            boxParam.put("A1",storeId);
            boxParam.put("A2",otherStoreId);
            boxParam.put("A3",boxCode);
            boxParam.put("A4",preSealBox.getUserId());
            boxParam.put("A7",preSealBox.getOrderId());

            //批量插入装箱商品
            int size = goodList.size();
            SqlParameterSource[] sqlParam = new MapSqlParameterSource[size];

            for(int i=0;i<size;i++){
                PreOrderGood good = goodList.get(i);
                boxParam.put("A5",good.getGoodId());
                boxParam.put("A6",good.getCount());

                sqlParam[i] = new MapSqlParameterSource(boxParam);
            }

            TransactionTemplate template = baseCustomDao.getTransactionTemplate(hikariDataSource);
            boolean flag = template.execute(status ->{
                try {

                    //按照商品批量插入
                    int[] box = jdbcTemplate.batchUpdate(INSERT_BOX_CODE,sqlParam);

                    //执行存储过程,更新数据库表
                    boolean temp = preAllotDao.goodIsInsert(jdbcTemplate,uuid,storeId);

                    if(box != null && box.length == size && temp){
                        return true;
                    }else{
                        status.setRollbackOnly();
                        if(box != null && box.length ==size){
                            log.error("调用存储过程更新数据库信息异常");
                        }else{
                            log.error("实际应插入数量 = 商品数量种类 = {}",size);
                            log.error("返回的插入数量={}",box == null ? null :box.length);
                        }
                        return false;
                    }

                } catch (SQLException e) {
                    status.setRollbackOnly();
                    log.error("插入箱码信息异常:{}", ExceptionUtils.getMessage(e));
                    return false;
                }
            });

            if(flag){
                return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"插入箱码信息成功",null);
            }else{
                return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"插入箱码信息失败",null);
            }

        } catch (SQLException e) {
            log.error("插入箱码信息异常：{}",ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"插入箱码信息异常",null);
        }finally {
            if(!hikariDataSource.isClosed()){
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }

    }

    @Override
    public List<PreBoxModel> getOutOrderDetail(String storeId, String orderId) {
        log.info("查询调出订单详情：{}",orderId);
        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

        try {
            //执行门店策略
            baseCustomDao.execScbh(jdbcTemplate,storeId);

            Map<String, String> parameterMap = new HashMap<>();
            parameterMap.put("ddh", orderId);

            //查询调出订单详情
            List<PreBoxModel> list = jdbcTemplate.query(SELECT_BOX_INFO,parameterMap, (resultSet, i) -> {
                PreBoxModel preOutOrderModel = new PreBoxModel();
                preOutOrderModel.setDocumentsId(resultSet.getString("djh"));     //单据号
                preOutOrderModel.setBoxCode(resultSet.getString("xmh"));           //箱码编号
                preOutOrderModel.setGoodKind(resultSet.getString("goodCount"));  //商品种类
                preOutOrderModel.setType(resultSet.getString("lx"));             //类型
                preOutOrderModel.setOtherStoreName(resultSet.getString("bmmc")); //对方门店名称
                return preOutOrderModel;

            });
            return list;

        } catch (Exception e) {
            log.error("查询订单：{}详情失败:{}",orderId,ExceptionUtils.getMessage(e));
            return null;
        }finally {
            if(!hikariDataSource.isClosed()){
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public List<PreOutGoodsModel> getBoxMsg(String storeId,String documentsId, String boxCode) {
        log.info("查询单据号为：{}里箱码为：{}的信息",documentsId,boxCode);

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

        try {
            //执行门店策略
            baseCustomDao.execScbh(jdbcTemplate,storeId);

            HashMap<String,String> paramMap = new HashMap();
            paramMap.put("djh",documentsId);
            paramMap.put("xmh",boxCode);

            //查询调出订单详情
            List<PreOutGoodsModel> list = jdbcTemplate.query(GOOD_INBOX_INFO,paramMap, (resultSet, i) -> {
                PreOutGoodsModel preOutGoodsModel = new PreOutGoodsModel();
                preOutGoodsModel.setGoodName(resultSet.getString("spmc"));  //商品名称
                preOutGoodsModel.setGoodId(resultSet.getString("glbh"));    //商品管理码
                preOutGoodsModel.setBoxCode(resultSet.getString("xmh"));     //箱码
                preOutGoodsModel.setOrderId(resultSet.getString("ddh"));     //订单编号
                preOutGoodsModel.setGoodCode(resultSet.getString("spbh"));    //商品条码
                preOutGoodsModel.setCountInBox(resultSet.getInt("zxsl"));     //箱中商品数量

                return preOutGoodsModel;

            });
            return list;

        } catch (Exception e) {
            log.error("查询箱码：{}详情失败:{}",boxCode,ExceptionUtils.getMessage(e));
            return null;
        }finally {
            if(!hikariDataSource.isClosed()){
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage delBox(String storeId, String documentsId, String boxCode) {
        log.info("删除箱码：{}",boxCode);

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

        try {
            preAllotDao.delBox(jdbcTemplate,storeId,documentsId,boxCode);

            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "删除箱码成功", null);
//            if(flag){
//
//            }else{
//                return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "删除箱码失败", null);
//            }

        }catch (Exception e){
            log.error("调用后台存储过程删除箱码失败：{}",ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "删除箱码失败", null);
        }
    }

    @Override
    public ResponseMessage delGoodInBox(String storeId, String documentsId, String boxCode, String goodId,String orderId) {
        log.info("删除箱中商品：{}",goodId);
        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

        try{
            preAllotDao.delGoodInBox(jdbcTemplate,storeId,documentsId,boxCode,goodId,orderId);

            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"删除箱中商品成功",null);
        }catch (Exception e){
            log.error("调用后台存储过程删除箱中商品失败：{}",ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "删除箱中商品失败", null);
        }

    }

    @Override
    public ResponseMessage finishPick(String storeId,String orderId,String userId){
        log.info("订单:{}分拣完成....",orderId);

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(storeId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

        try{
            //执行存储过程
            preAllotDao.finishPicked(jdbcTemplate,storeId,orderId,userId);

            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"订单修改状态成功",null);
        }catch (Exception e){
            log.error("订单：{}状态修改失败：{}",orderId,ExceptionUtils.getMessage(e));

            return ResponseMessage.responseMessage(ResultCode.FAIL.code(),"订单状态修改失败",null);
        }

    }


}
