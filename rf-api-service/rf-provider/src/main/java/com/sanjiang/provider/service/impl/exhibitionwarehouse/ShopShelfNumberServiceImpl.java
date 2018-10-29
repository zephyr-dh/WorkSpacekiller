package com.sanjiang.provider.service.impl.exhibitionwarehouse;

import com.alibaba.dubbo.config.annotation.Service;
import com.sanjiang.constrants.DefaultValue;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.core.ResultCode;
import com.sanjiang.provider.constrants.CacheType;
import com.sanjiang.provider.constrants.ShelfAreaType;
import com.sanjiang.provider.constrants.ShelfType;
import com.sanjiang.provider.constrants.SqlParam;
import com.sanjiang.provider.domain.exhibitionwarehouse.ShopShelfNumber;
import com.sanjiang.provider.model.ShelfProductCountModel;
import com.sanjiang.provider.model.ShelfTypeModel;
import com.sanjiang.provider.model.exhibitionwarehouse.ShelfConstant;
import com.sanjiang.provider.service.exhibitionwarehouse.ShopShelfNumberService;
import com.sanjiang.provider.util.BaseCustomDao;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 门店货架服务
 *
 * @author kimiyu
 * @date 2018/4/26 10:42
 */
@Service(
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}",
        group = "shelf",
        version = "1.0.0",
        timeout = 50000
)
@Slf4j
public class ShopShelfNumberServiceImpl implements ShopShelfNumberService {

    @Autowired
    private BaseCustomDao baseCustomDao;

    private static final String SHOP_SHELF_NUMBER_INSERT_NUMBER = "insert into hj_bh_sc (scbh,hjbh,hjqy,hjlx) values (:as_scbh, :as_hjbh, :as_hyqy, :as_hjlx)";
    private static final String SHOP_SHELF_NUMBER_SELECT_NUMBER = "select hjqy, hjlx, xgrq from hj_bh_sc where scbh=:as_scbh and hjbh=:as_hjbh";

    private static final String COUNT_LOCATION_LAYERNUMBER_SQL = "SELECT count(distinct ch) as_ch, count(wz) as_wz from HJ_SP_SC where HJ_SP_SC.SCBH=:as_scbh and hjbh=:as_hjbh";

    private static final String COUNT_SHOP_LOCATION_LAYERNUMBER_SQL = "SELECT count(distinct ch) ch, count(wz) wz,HJBH from HJ_SP_SC where HJ_SP_SC.SCBH=:as_scbh group by HJBH";

    private static final String DELETE_SHOP_SHELF_SQL = "delete from hj_bh_sc where scbh=:as_scbh and hjbh=:as_hjbh";

    private static final String DELETE_SHOP_SHELF_PRODUCT_SQL = "delete from hj_sp_sc where scbh=:as_scbh and hjbh=:as_hjbh";

    private static final String LIST_SHELF_TYPE_SQL = "select * from dm_code where app='HJ' and usefor in (:as_type,:as_area)";

    // 货架号默认按倒叙排列
    private static final String LIST_SHELF_SQL = "select * from hj_bh_sc where scbh=:as_scbh order by xgrq desc ";


    @Override
    public ResponseMessage save(ShopShelfNumber shopShelfNumber) {
        String shopId = shopShelfNumber.getShopId();

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(shopId);
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            // 执行存储过程
            baseCustomDao.execScbh(namedParameterJdbcTemplate, shopId);

            Map<String, String> parameterMap = new HashMap<>();
            parameterMap.put(SqlParam.AS_SCBH.value(), shopId);
            parameterMap.put(SqlParam.AS_HJBH.value(), shopShelfNumber.getShelfNumber());
            parameterMap.put("as_hyqy", shopShelfNumber.getShelfArea());
            parameterMap.put("as_hjlx", shopShelfNumber.getShelfDisplayType());
            SqlParameterSource namedParameters = new MapSqlParameterSource(parameterMap);

            TransactionTemplate transactionTemplate = baseCustomDao.getTransactionTemplate(hikariDataSource);
            Integer result = transactionTemplate.execute(status -> {
                try {
                    return namedParameterJdbcTemplate.update(SHOP_SHELF_NUMBER_INSERT_NUMBER, namedParameters);
                } catch (Exception ex) {
                    status.setRollbackOnly();
                    log.warn("陈列仓保存异常:{}", ExceptionUtils.getMessage(ex));
                    return null;
                }
            });

            return null == result ?
                    ResponseMessage.responseMessage(ResultCode.FAIL.code(), "陈列仓保存失败", null) :
                    ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "陈列仓保存成功", null);
        } catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "陈列仓保存失败", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }


    @Override
    public ResponseMessage findByShopIdAndShelfNumber(String shopId, String shelfNumber) {

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(shopId);
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            // 执行存储过程
            baseCustomDao.execScbh(namedParameterJdbcTemplate, shopId);

            Map<String, String> parameterMap = new HashMap<>();
            parameterMap.put(SqlParam.AS_SCBH.value(), shopId);
            parameterMap.put(SqlParam.AS_HJBH.value(), shelfNumber);

            ShopShelfNumber shopShelfNumber = namedParameterJdbcTemplate.queryForObject(SHOP_SHELF_NUMBER_SELECT_NUMBER, parameterMap, (rs, rowNum) -> {
                ShopShelfNumber shopShelfNumber1 = new ShopShelfNumber();
                shopShelfNumber1.setShelfNumber(shelfNumber);
                shopShelfNumber1.setShopId(shopId);
                shopShelfNumber1.setShelfDisplayType(rs.getString("hjlx"));
                shopShelfNumber1.setShelfArea(rs.getString("hjqy"));
                shopShelfNumber1.setOperateDate(rs.getTimestamp("xgrq"));
                return shopShelfNumber1;
            });

            if (null == shopShelfNumber) {
                return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "没有找到对应的货架", null);
            }

            Map<String, Object> totalShelfMap = totalShelfMap(shopId, shelfNumber, namedParameterJdbcTemplate);
            shopShelfNumber.setLayerNumber(Integer.valueOf(totalShelfMap.get(SqlParam.AS_CH.value()).toString()));
            shopShelfNumber.setProductNumber(Integer.valueOf(totalShelfMap.get(SqlParam.AS_WZ.value()).toString()));

            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "", shopShelfNumber);
        } catch (Exception ex) {
            log.info("货架查询异常：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "货架查询异常", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }


    /**
     * 根据门店编号获取货架常量列表
     *
     * @param shopId
     * @return
     */
    @Override
    @Cacheable(cacheNames = CacheType.SHELF_ATTRIBUTE_CACHE)
    public ResponseMessage getShelfConstants(String shopId) {

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(shopId);
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            // 执行存储过程
            baseCustomDao.execScbh(namedParameterJdbcTemplate, shopId);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("as_type", "HJLX");
            paramMap.put("as_area", "HJQY");

            List<ShelfTypeModel> shelfTypeModels = namedParameterJdbcTemplate.query(LIST_SHELF_TYPE_SQL, paramMap, new RowMapper<ShelfTypeModel>() {
                @Override
                public ShelfTypeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                    ShelfTypeModel shelfTypeModel = new ShelfTypeModel();
                    shelfTypeModel.setCode(rs.getString("VAL"));
                    shelfTypeModel.setDisplay(rs.getString("DISPLAY"));
                    shelfTypeModel.setType(rs.getString("USEFOR"));
                    return shelfTypeModel;
                }
            });

            return generateShelfTypes(shelfTypeModels);
        } catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return generateShelfTypes(new ArrayList<>());
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }


    }

    @Override
    public ResponseMessage getShelfs(String shopId) {
        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(shopId);
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

            baseCustomDao.execScbh(namedParameterJdbcTemplate, shopId);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(SqlParam.AS_SCBH.value(), shopId);

            List<ShopShelfNumber> shopShelfNumbers = namedParameterJdbcTemplate.query(LIST_SHELF_SQL, paramMap, new RowMapper<ShopShelfNumber>() {
                @Override
                public ShopShelfNumber mapRow(ResultSet rs, int rowNum) throws SQLException {
                    ShopShelfNumber shopShelfNumber = new ShopShelfNumber();
                    shopShelfNumber.setShelfNumber(rs.getString("hjbh"));
                    shopShelfNumber.setShopId(shopId);
                    shopShelfNumber.setShelfDisplayType(rs.getString("hjlx"));
                    shopShelfNumber.setShelfArea(rs.getString("hjqy"));
                    shopShelfNumber.setOperateDate(rs.getTimestamp("xgrq"));
                    return shopShelfNumber;
                }
            });

            if (CollectionUtils.isEmpty(shopShelfNumbers)) {
                return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "", shopShelfNumbers);
            }
            Map<String, ShelfProductCountModel> shelfProductCountModelMap = totalShelfMap(shopId, namedParameterJdbcTemplate);

            // 查询货架的商品数量和总层数
            shopShelfNumbers.forEach(shopShelfNumber -> {
                String shelfNumber = shopShelfNumber.getShelfNumber();
                ShelfProductCountModel shelfProductCountModel = shelfProductCountModelMap.get(shelfNumber);
                if (shelfProductCountModel != null) {
                    shopShelfNumber.setProductNumber(shelfProductCountModel.getTotalLocation());
                    shopShelfNumber.setLayerNumber(shelfProductCountModel.getTotalLayerNumber());
                }
            });

            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "", shopShelfNumbers);


        } catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    /**
     * 生成陈列
     *
     * @param shelfTypeModels
     * @return
     */
    private ResponseMessage generateShelfTypes(List<ShelfTypeModel> shelfTypeModels) {
        List<ShelfConstant<Map<String, String>>> shelfConstants = new ArrayList<>();
        if (CollectionUtils.isEmpty(shelfTypeModels)) {
            ShelfConstant<Map<String, String>> shelfAreaTypeConstant = new ShelfConstant<>();
            shelfAreaTypeConstant.setCode("HJQY");
            shelfAreaTypeConstant.setValue("货架区域");
            Map<String, String> shelfAreaMap = new HashMap<>();
            shelfAreaMap.put(ShelfAreaType.SHELF.value(), "货架");
            shelfAreaMap.put(ShelfAreaType.PILE_POSITION.value(), "堆位");
            shelfAreaMap.put(ShelfAreaType.END_FRAME.value(), "端架");
            shelfAreaMap.put(ShelfAreaType.STORAGE_ONE.value(), "仓库1");
            shelfAreaMap.put(ShelfAreaType.STORAGE_TWO.value(), "仓库2");
            shelfAreaTypeConstant.setData(shelfAreaMap);
            shelfConstants.add(shelfAreaTypeConstant);

            ShelfConstant<Map<String, String>> shelfTypeConstant = new ShelfConstant<>();
            shelfTypeConstant.setCode("HJLX");
            shelfTypeConstant.setValue("陈列方式");
            Map<String, String> shelfTypeMap = new HashMap<>();
            shelfTypeMap.put(ShelfType.LAMINATE.value(), "层板");
            shelfTypeMap.put(ShelfType.BASKET.value(), "网篮");
            shelfTypeMap.put(ShelfType.POTHOOK.value(), "挂钩");
            shelfTypeMap.put(ShelfType.PROMOTION_POSITION.value(), "促销位");
            shelfTypeMap.put(ShelfType.COLD_WIND.value(), "冷风");
            shelfTypeMap.put(ShelfType.ICE_TANK.value(), "冰柜");
            shelfTypeConstant.setData(shelfTypeMap);
            shelfConstants.add(shelfTypeConstant);
        } else {
            ShelfConstant<Map<String, String>> shelfAreaTypeConstant = new ShelfConstant<>();
            shelfAreaTypeConstant.setCode("HJQY");
            shelfAreaTypeConstant.setValue("货架区域");
            Map<String, String> shelfAreaMap = new HashMap<>();
            shelfTypeModels
                    .stream()
                    .filter(shelfTypeModel -> shelfTypeModel.getType().equals("HJQY"))
                    .forEach(shelfTypeModel -> {
                        shelfAreaMap.put(shelfTypeModel.getCode(), shelfTypeModel.getDisplay());
                    });
            shelfAreaTypeConstant.setData(shelfAreaMap);
            shelfConstants.add(shelfAreaTypeConstant);

            ShelfConstant<Map<String, String>> shelfTypeConstant = new ShelfConstant<>();
            shelfTypeConstant.setCode("HJLX");
            shelfTypeConstant.setValue("陈列方式");
            Map<String, String> shelfTypeMap = new HashMap<>();
            shelfTypeModels.stream().filter(shelfTypeModel -> shelfTypeModel.getType().equals("HJLX"))
                    .forEach(shelfTypeModel -> {
                        shelfTypeMap.put(shelfTypeModel.getCode(), shelfTypeModel.getDisplay());
                    });
            shelfTypeConstant.setData(shelfTypeMap);
            shelfConstants.add(shelfTypeConstant);
        }

        return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "", shelfConstants);

    }

    /**
     * 清空货架信息
     *
     * @param shopId      门店ID
     * @param shelfNumber 货架编号
     * @return
     */
    @Override
    public ResponseMessage cleanShelf(String shopId, String shelfNumber) {

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(shopId);
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            // 执行存储过程
            baseCustomDao.execScbh(namedParameterJdbcTemplate, shopId);

            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(SqlParam.AS_SCBH.value(), shopId);
            paramMap.put(SqlParam.AS_HJBH.value(), shelfNumber);

            TransactionTemplate transactionTemplate = baseCustomDao.getTransactionTemplate(hikariDataSource);
            Integer result = transactionTemplate.execute(status -> {
                try {
                    namedParameterJdbcTemplate.update(DELETE_SHOP_SHELF_PRODUCT_SQL, paramMap);
                    return namedParameterJdbcTemplate.update(DELETE_SHOP_SHELF_SQL, paramMap);
                } catch (Exception ex) {
                    status.setRollbackOnly();
                    log.warn("陈列仓保存异常:{}", ExceptionUtils.getMessage(ex));
                    return null;
                }
            });

            return null == result ?
                    ResponseMessage.responseMessage(ResultCode.FAIL.code(), "清空货架信息失败!", null) :
                    ResponseMessage.responseMessage(ResultCode.FAIL.code(), "清空货架信息成功!", null);
        } catch (SQLException ex) {
            log.warn("清空货架信息失败：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "清空货架信息失败!", null);
        }

    }

    /**
     * @param shopId
     * @param shelfNumber
     * @param namedParameterJdbcTemplate
     * @return
     */
    private Map<String, Object> totalShelfMap(String shopId, String shelfNumber, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(SqlParam.AS_SCBH.value(), shopId);
        paramMap.put(SqlParam.AS_HJBH.value(), shelfNumber);

        return namedParameterJdbcTemplate.queryForMap(COUNT_LOCATION_LAYERNUMBER_SQL, paramMap);
    }


    /**
     * @param shopId
     * @param namedParameterJdbcTemplate
     * @return
     */
    private Map<String, ShelfProductCountModel> totalShelfMap(String shopId, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(SqlParam.AS_SCBH.value(), shopId);

        List<ShelfProductCountModel> shelfProductCountModels = namedParameterJdbcTemplate.query(COUNT_SHOP_LOCATION_LAYERNUMBER_SQL, paramMap, new RowMapper<ShelfProductCountModel>() {
            @Override
            public ShelfProductCountModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                ShelfProductCountModel shelfProductCountModel = new ShelfProductCountModel();
                shelfProductCountModel.setShelfNumber(rs.getString("hjbh"));
                shelfProductCountModel.setTotalLayerNumber(rs.getInt("ch"));
                shelfProductCountModel.setTotalLocation(rs.getInt("wz"));

                return shelfProductCountModel;
            }
        });

        if (CollectionUtils.isEmpty(shelfProductCountModels)) {
            return new HashMap<>();
        }

        return shelfProductCountModels.stream().collect(Collectors.toMap(ShelfProductCountModel::getShelfNumber, Function.identity()));
    }
}
