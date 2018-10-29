package com.sanjiang.provider.service.impl.exhibitionwarehouse;

import com.alibaba.dubbo.config.annotation.Service;
import com.sanjiang.constrants.DefaultValue;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.core.ResultCode;
import com.sanjiang.provider.constrants.SqlParam;
import com.sanjiang.provider.domain.exhibitionwarehouse.ShopShelfProduct;
import com.sanjiang.provider.service.exhibitionwarehouse.ShopShelfProductService;
import com.sanjiang.provider.util.BaseCustomDao;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.*;

/**
 * 商品货架
 *
 * @author kimiyu
 * @date 2018/4/26 18:35
 */
@Service(
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}",
        version = "1.0.0",
        group = "shelf-product",
        timeout = 50000
)
@Slf4j
public class ShopShelfProductServiceImpl implements ShopShelfProductService {

    @Autowired
    private BaseCustomDao baseCustomDao;


    private static final String SERIAL_NUMBER_SQL = "select hj_sp_sc_seq.nextval from dual";
    // 新增总层数字段
    private static final String TOTAL_LAYER_NUMBER_SQL = "SELECT max(to_number(ch)) from hj_sp_sc where scbh=:as_scbh and hjbh=:as_hjbh";

    private static final String CHECK_LOCATION_SQL = "SELECT count(wz) from hj_sp_sc where scbh=:as_scbh and hjbh=:as_hjbh and wz=:as_wz and ch=:as_ch";

    // 新增商品位置字段
    private static final String TOTAL_LOCATION_SQL = "SELECT max(to_number(wz)) from hj_sp_sc where scbh=:as_scbh and hjbh=:as_hjbh and ch=:as_ch";


    private static final String INSERT_SHOP_SHELF_PRODUCT_SQL = "insert into hj_sp_sc (scbh,hjbh, ch,wz,glbh,lsh,spbh) values (:as_scbh,:as_hjbh,:as_ch, :as_wz,:as_glbh,:al_sp_seq,:as_spbh)";

    private static final String BULK_INSERT_SHOP_SHELF_PRODUCT_SQL = "insert into hj_sp_sc (scbh,hjbh, ch,wz,glbh,xgrq,lsh,spbh) values (:as_scbh,:as_hjbh,:as_ch, :as_wz,:as_glbh,:operate_date,:al_sp_seq,:as_spbh)";


    private static final String LIST_SHOP_SHELF_PRODUCT_SQL = "select a.wz,a.glbh,spbh,spmc,jzhyj,a.lsh,a.xgrq from hj_sp_sc a, user_xt_spda b where a.scbh=:as_scbh and a.hjbh=:as_hjbh and a.ch=:as_ch and a.glbh=b.glbh order by to_number(wz)";

    private static final String LIST_HJBH_SHOP_SHELF_PRODUCT_LOCATION_SQL = "select hjbh,ch,wz,glbh,spbh from hj_sp_sc where scbh=:as_scbh and (glbh=:as_glbh or spbh=:as_spbh) and hjbh=:as_hjbh order by to_number(ch),to_number(wz)";

    private static final String LIST_SHOP_SHELF_PRODUCT_LOCATION_SQL = "select hjbh,ch,wz,glbh,spbh from hj_sp_sc where scbh=:as_scbh and (glbh=:as_glbh or spbh=:as_spbh) order by to_number(ch),to_number(wz)";


    private static final String List_HJBH_SHOP_SHELF_PRODUCT_SQL = "select glbh,wz,ch,xgrq,lsh,spbh from hj_sp_sc where scbh=:as_scbh and hjbh=:as_hjbh and ch=:as_ch order by to_number(wz)";

    private static final String DELETE_LAYER_NUMBER_SQL = "delete hj_sp_sc where scbh=:as_scbh and hjbh=:as_hjbh and ch=:as_ch";

    private static final String DELETE_SERIAL_NUMBER_SQL = "delete hj_sp_sc where scbh=:as_scbh and hjbh=:as_hjbh and lsh=:as_lsh";


    @Override
    public ResponseMessage save(ShopShelfProduct shopShelfProduct) {

        String shopId = shopShelfProduct.getShopId();

        String shelfNumber = shopShelfProduct.getShelfNumber();

        Integer layerNumber = shopShelfProduct.getLayerNumber();

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(shopId);
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            // 执行存储过程
            baseCustomDao.execScbh(namedParameterJdbcTemplate, shopId);

            // 无效层级或层级为空，取层号最大值
            if (null == layerNumber) {
                layerNumber = getLayerNumber(shopId, shelfNumber, namedParameterJdbcTemplate);
                if (null == layerNumber) {
                    layerNumber = 1;
                }
            }

            // 校验位置是否有效
            boolean reorder = false;
            Integer location = shopShelfProduct.getLocation();
            if (location != null) {
                Integer validLocation = validLocation(shopId, shelfNumber, layerNumber, location, namedParameterJdbcTemplate);
                if (validLocation != null && !(validLocation.compareTo(1) < 0)) {
                    reorder = true;
                }
            }
            if (null == location) {
                location = getLocation(shopId, shelfNumber, layerNumber, namedParameterJdbcTemplate);
                location = null == location ? 1 : location + 1;
            }

            shopShelfProduct.setLayerNumber(layerNumber);
            shopShelfProduct.setLocation(location);
            shopShelfProduct.setSerialNumber(getSerialNumber(namedParameterJdbcTemplate));
            if (!reorder) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put(SqlParam.AS_SCBH.value(), shopId);
                paramMap.put(SqlParam.AS_HJBH.value(), shelfNumber);
                paramMap.put(SqlParam.AS_CH.value(), layerNumber);
                paramMap.put("as_wz", shopShelfProduct.getLocation());
                paramMap.put(SqlParam.AS_GLBH.value(), shopShelfProduct.getErpGoodsId());
                paramMap.put("al_sp_seq", shopShelfProduct.getSerialNumber());
                paramMap.put(SqlParam.AS_SPBH.value(), shopShelfProduct.getBarCode());

                SqlParameterSource sqlParameterSource = new MapSqlParameterSource(paramMap);

                log.info("要保存的商品信息：{}", shopShelfProduct.toString());

                Integer result = baseCustomDao.operateSQL(namedParameterJdbcTemplate, sqlParameterSource, hikariDataSource, INSERT_SHOP_SHELF_PRODUCT_SQL);
                return (null == result) ?
                        ResponseMessage.responseMessage(ResultCode.FAIL.code(), "货架商品保存失败！", null) :
                        ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "货架商品保存成功！", null);
            }

            List<ShopShelfProduct> shopShelfProducts = getSortedShopShelfProducts(shopId, shelfNumber, layerNumber, namedParameterJdbcTemplate);
            int size = shopShelfProducts.size();
            int waitToInsertLocation = getIndexOfShelfProduct(shopShelfProducts, 0, size - 1, location);

            Calendar nowTime = Calendar.getInstance();
            shopShelfProduct.setOperateDate(new Timestamp(nowTime.getTime().getTime()));
            if (waitToInsertLocation < 0) {
                waitToInsertLocation = size;
            }
            shopShelfProducts.add(waitToInsertLocation, shopShelfProduct);

            size = shopShelfProducts.size();
            for (int i = 0; i < size; i++) {
                shopShelfProducts.get(i).setLocation(i + 1);
            }


            SqlParameterSource[] sqlParameterSources = getSqlParameterSources(shopId, shelfNumber, layerNumber, shopShelfProducts, size);

            TransactionTemplate transactionTemplate = baseCustomDao.getTransactionTemplate(hikariDataSource);
            Map<String, Object> delParamMap = new HashMap<>();
            delParamMap.put(SqlParam.AS_SCBH.value(), shopId);
            delParamMap.put(SqlParam.AS_HJBH.value(), shelfNumber);
            delParamMap.put(SqlParam.AS_CH.value(), layerNumber);

            int[] resultArray = operateBulkProducts(namedParameterJdbcTemplate, sqlParameterSources, transactionTemplate, delParamMap);

            int resultLenth = null == resultArray ? 0 : resultArray.length;

            return (resultLenth != size) ?
                    ResponseMessage.responseMessage(ResultCode.FAIL.code(), "货架商品保存失败！", null) :
                    ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "货架商品保存成功！", null);


        } catch (SQLException ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "货架商品保存失败！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }


    }

    @Override
    public ResponseMessage<List<ShopShelfProduct>> list(String shopId, String shelfNumber, Integer layerNumber, boolean operateDelete) {

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(shopId);
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            // 执行存储过程
            baseCustomDao.execScbh(namedParameterJdbcTemplate, shopId);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(SqlParam.AS_SCBH.value(), shopId);
            paramMap.put(SqlParam.AS_HJBH.value(), shelfNumber);
            paramMap.put(SqlParam.AS_CH.value(), layerNumber);

            // 删除操作重新排序
            if (operateDelete) {
                List<ShopShelfProduct> sortedShopShelfProducts = getSortedShopShelfProducts(shopId, shelfNumber, layerNumber, namedParameterJdbcTemplate);
                if (CollectionUtils.isEmpty(sortedShopShelfProducts)) {
                    return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "", null);
                }

                int sortedSize = sortedShopShelfProducts.size();
                for (int i = 0; i < sortedSize; i++) {
                    sortedShopShelfProducts.get(i).setLocation(i + 1);
                }

                SqlParameterSource[] sqlParameterSources = getSqlParameterSources(shopId, shelfNumber, layerNumber, sortedShopShelfProducts, sortedSize);

                TransactionTemplate transactionTemplate = baseCustomDao.getTransactionTemplate(hikariDataSource);
                Map<String, Object> delParamMap = new HashMap<>();
                delParamMap.put(SqlParam.AS_SCBH.value(), shopId);
                delParamMap.put(SqlParam.AS_HJBH.value(), shelfNumber);
                delParamMap.put(SqlParam.AS_CH.value(), layerNumber);

                int[] resultArray = operateBulkProducts(namedParameterJdbcTemplate, sqlParameterSources, transactionTemplate, delParamMap);

                int resultLenth = null == resultArray ? 0 : resultArray.length;
                if (sortedSize != resultLenth) {
                    log.warn("{}门店，{}货架的第{}层商品获取异常！", shopId, shelfNumber, layerNumber);
                    return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "获取列表异常！", null);
                }
            }

            List<ShopShelfProduct> shopShelfProducts = namedParameterJdbcTemplate.query(LIST_SHOP_SHELF_PRODUCT_SQL, paramMap, (rs, rowNum) -> {
                ShopShelfProduct shopShelfProduct = new ShopShelfProduct();
                shopShelfProduct.setShopId(shopId);
                shopShelfProduct.setErpGoodsId(rs.getString("GLBH"));
                shopShelfProduct.setShelfNumber(shelfNumber);
                shopShelfProduct.setLayerNumber(layerNumber);
                shopShelfProduct.setLocation(rs.getInt("WZ"));
                shopShelfProduct.setProductName(rs.getString("SPMC"));
                shopShelfProduct.setBarCode(rs.getString("spbh"));
                String memberPriceStr = rs.getString(SqlParam.AS_JZHYJ.value());
                if (StringUtils.isNotEmpty(memberPriceStr)) {
                    memberPriceStr = new BigDecimal(memberPriceStr).setScale(2, RoundingMode.HALF_UP).toString();
                }
                shopShelfProduct.setMemberPrice(memberPriceStr);
                shopShelfProduct.setSerialNumber(rs.getLong("lsh"));
                shopShelfProduct.setOperateDate(rs.getTimestamp("xgrq"));

                return shopShelfProduct;
            });

            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "", shopShelfProducts);

        } catch (SQLException ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "货架商品列表为空！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage deleteByLayerNumber(String shopId, String shelfNumber, Integer layerNumber) {
        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(shopId);
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            // 执行存储过程
            baseCustomDao.execScbh(namedParameterJdbcTemplate, shopId);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(SqlParam.AS_SCBH.value(), shopId);
            paramMap.put(SqlParam.AS_HJBH.value(), shelfNumber);
            paramMap.put(SqlParam.AS_CH.value(), layerNumber);

            SqlParameterSource sqlParameterSource = new MapSqlParameterSource(paramMap);

            Integer result = baseCustomDao.operateSQL(namedParameterJdbcTemplate, sqlParameterSource, hikariDataSource, DELETE_LAYER_NUMBER_SQL);
            return (null == result) ?
                    ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "按层号删除数据失败！", null) :
                    ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "删除成功！", null);

        } catch (SQLException ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "按层号删除数据失败！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage deleteById(String shopId, String shelfNumber, Long serialNumber) {
        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(shopId);
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            // 执行存储过程
            baseCustomDao.execScbh(namedParameterJdbcTemplate, shopId);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(SqlParam.AS_SCBH.value(), shopId);
            paramMap.put(SqlParam.AS_HJBH.value(), shelfNumber);
            paramMap.put("as_lsh", serialNumber);

            SqlParameterSource sqlParameterSource = new MapSqlParameterSource(paramMap);

            Integer result = baseCustomDao.operateSQL(namedParameterJdbcTemplate, sqlParameterSource, hikariDataSource, DELETE_SERIAL_NUMBER_SQL);
            return null == result ? ResponseMessage.responseMessage(ResultCode.FAIL.code(), "按序列号删除失败！", null) :
                    ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "删除成功！", null);

        } catch (SQLException ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "按序列号删除失败！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage deleteByIdList(String shopId, String shelfNumber, List<Long> serialNumbers) {

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(shopId);
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            // 执行存储过程
            baseCustomDao.execScbh(namedParameterJdbcTemplate, shopId);

            int size = serialNumbers.size();
            SqlParameterSource[] sqlParameterSources = new MapSqlParameterSource[size];
            for (int i = 0; i < size; i++) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put(SqlParam.AS_SCBH.value(), shopId);
                paramMap.put(SqlParam.AS_HJBH.value(), shelfNumber);
                paramMap.put("as_lsh", serialNumbers.get(i));
                sqlParameterSources[i] = new MapSqlParameterSource(paramMap);
            }

            int[] result = baseCustomDao.batchSQL(namedParameterJdbcTemplate, sqlParameterSources, hikariDataSource, DELETE_SERIAL_NUMBER_SQL);
            return null == result || result.length != size ? ResponseMessage.responseMessage(ResultCode.FAIL.code(), "按序列号列表删除失败！", null) :
                    ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "按序列号列表删除成功！", null);

        } catch (SQLException ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "按序列号列表删除失败！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage<List<ShopShelfProduct>> getLocationList(String shopId, String shelfNumber, String searchParam) {
        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(shopId);
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            // 执行存储过程
            baseCustomDao.execScbh(namedParameterJdbcTemplate, shopId);

            String sql = LIST_SHOP_SHELF_PRODUCT_LOCATION_SQL;

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(SqlParam.AS_SCBH.value(), shopId);
            int length = searchParam.length();
            if (length == 7) {
                paramMap.put(SqlParam.AS_GLBH.value(), searchParam);
                paramMap.put(SqlParam.AS_SPBH.value(), "");
            } else {
                paramMap.put(SqlParam.AS_SPBH.value(), searchParam);
                paramMap.put(SqlParam.AS_GLBH.value(), "");
            }

            if (StringUtils.isNotEmpty(shelfNumber)) {
                paramMap.put(SqlParam.AS_HJBH.value(), shelfNumber);
                sql = LIST_HJBH_SHOP_SHELF_PRODUCT_LOCATION_SQL;
            }


            SqlParameterSource sqlParameterSource = new MapSqlParameterSource(paramMap);

            List<ShopShelfProduct> shopShelfProducts = namedParameterJdbcTemplate.query(sql, sqlParameterSource, (rs, rowNum) -> {
                ShopShelfProduct shopShelfProduct = new ShopShelfProduct();
                shopShelfProduct.setShopId(shopId);
                shopShelfProduct.setBarCode(rs.getString("spbh"));
                shopShelfProduct.setErpGoodsId(rs.getString("glbh"));
                shopShelfProduct.setShelfNumber(rs.getString("hjbh"));
                shopShelfProduct.setLocation(rs.getInt("wz"));
                shopShelfProduct.setLayerNumber(rs.getInt("ch"));

                return shopShelfProduct;
            });

            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "", shopShelfProducts);

        } catch (SQLException ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "货架商品位置列表为空！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }


    /**
     * 获取层号
     *
     * @param shopId
     * @param shelfNumber
     * @param namedParameterJdbcTemplate
     * @return
     */
    private Integer getLayerNumber(String shopId, String shelfNumber, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(SqlParam.AS_SCBH.value(), shopId);
        paramMap.put(SqlParam.AS_HJBH.value(), shelfNumber);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(paramMap);

        return namedParameterJdbcTemplate.queryForObject(TOTAL_LAYER_NUMBER_SQL, sqlParameterSource, Integer.class);
    }

    /**
     * 校验位置
     *
     * @param shopId
     * @param shelfNumber
     * @param namedParameterJdbcTemplate
     * @return
     */
    private Integer validLocation(String shopId, String shelfNumber, Integer layerNumber, Integer location, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(SqlParam.AS_SCBH.value(), shopId);
        paramMap.put(SqlParam.AS_HJBH.value(), shelfNumber);
        paramMap.put(SqlParam.AS_CH.value(), layerNumber);
        paramMap.put(SqlParam.AS_WZ.value(), location);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(paramMap);
        try {
            return namedParameterJdbcTemplate.queryForObject(CHECK_LOCATION_SQL, sqlParameterSource, Integer.class);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }


    /**
     * 获取商品位置
     *
     * @param shopId
     * @param shelfNumber
     * @param layerNumber
     * @param namedParameterJdbcTemplate
     * @return
     */
    private Integer getLocation(String shopId, String shelfNumber, Integer layerNumber, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(SqlParam.AS_SCBH.value(), shopId);
        paramMap.put(SqlParam.AS_HJBH.value(), shelfNumber);
        paramMap.put(SqlParam.AS_CH.value(), layerNumber);

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(paramMap);

        return namedParameterJdbcTemplate.queryForObject(TOTAL_LOCATION_SQL, sqlParameterSource, Integer.class);
    }


    /**
     * 获取流水号
     *
     * @return
     */
    private Long getSerialNumber(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

        return namedParameterJdbcTemplate.queryForObject(SERIAL_NUMBER_SQL, EmptySqlParameterSource.INSTANCE, Long.class);
    }

    /**
     * 批处理商品
     *
     * @param namedParameterJdbcTemplate
     * @param sqlParameterSources
     * @param transactionTemplate
     * @param delParamMap
     * @return
     */
    private int[] operateBulkProducts(NamedParameterJdbcTemplate namedParameterJdbcTemplate, SqlParameterSource[] sqlParameterSources, TransactionTemplate transactionTemplate, Map<String, Object> delParamMap) {
        return transactionTemplate.execute(status -> {
            try {
                // 删除当前层
                namedParameterJdbcTemplate.update(DELETE_LAYER_NUMBER_SQL, delParamMap);
                // 重新排序
                return namedParameterJdbcTemplate.batchUpdate(BULK_INSERT_SHOP_SHELF_PRODUCT_SQL, sqlParameterSources);
            } catch (Exception ex) {
                status.setRollbackOnly();
                log.info("批处理SQL异常：{}", ExceptionUtils.getMessage(ex));
                return null;
            }
        });
    }

    /**
     * 按顺序获取
     *
     * @param shopId
     * @param shelfNumber
     * @param layerNumber
     * @param namedParameterJdbcTemplate
     * @return
     */
    private List<ShopShelfProduct> getSortedShopShelfProducts(String shopId, String shelfNumber, Integer layerNumber, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(SqlParam.AS_SCBH.value(), shopId);
        paramMap.put(SqlParam.AS_HJBH.value(), shelfNumber);
        paramMap.put(SqlParam.AS_CH.value(), layerNumber);
        List<ShopShelfProduct> shopShelfProducts = namedParameterJdbcTemplate.query(List_HJBH_SHOP_SHELF_PRODUCT_SQL, paramMap, (rs, rowNum) -> {
            ShopShelfProduct shopShelfProduct1 = new ShopShelfProduct();
            shopShelfProduct1.setShopId(shopId);
            shopShelfProduct1.setErpGoodsId(rs.getString("GLBH"));
            shopShelfProduct1.setShelfNumber(shelfNumber);
            shopShelfProduct1.setLayerNumber(rs.getInt("ch"));
            shopShelfProduct1.setLocation(rs.getInt("WZ"));
            shopShelfProduct1.setOperateDate(rs.getTimestamp("xgrq"));
            shopShelfProduct1.setSerialNumber(rs.getLong("lsh"));
            shopShelfProduct1.setBarCode(rs.getString("spbh"));
            return shopShelfProduct1;
        });

        // 按位置顺序排列
        shopShelfProducts.sort(Comparator.comparing(ShopShelfProduct::getLocation));
        return shopShelfProducts;
    }

    /**
     * 批处理sql参数
     *
     * @param shopId
     * @param shelfNumber
     * @param layerNumber
     * @param sortedShopShelfProducts
     * @param sortedSize
     * @return
     */
    private SqlParameterSource[] getSqlParameterSources(String shopId, String shelfNumber, Integer layerNumber, List<ShopShelfProduct> sortedShopShelfProducts, int sortedSize) {
        SqlParameterSource[] sqlParameterSources = new MapSqlParameterSource[sortedSize];
        ZoneId zoneId = ZoneId.systemDefault();
        for (int i = 0; i < sortedSize; i++) {
            ShopShelfProduct shelfProduct = sortedShopShelfProducts.get(i);
            Map<String, Object> bulkParamMap = new HashMap<>();
            bulkParamMap.put(SqlParam.AS_SCBH.value(), shopId);
            bulkParamMap.put(SqlParam.AS_HJBH.value(), shelfNumber);
            bulkParamMap.put(SqlParam.AS_CH.value(), layerNumber);
            bulkParamMap.put(SqlParam.AS_WZ.value(), shelfProduct.getLocation());
            bulkParamMap.put(SqlParam.AS_GLBH.value(), shelfProduct.getErpGoodsId());
            bulkParamMap.put(SqlParam.AS_SPBH.value(), shelfProduct.getBarCode());
            bulkParamMap.put("al_sp_seq", shelfProduct.getSerialNumber());
            bulkParamMap.put("operate_date", Timestamp.from(shelfProduct.getOperateDate().atZone(zoneId).toInstant()));
            sqlParameterSources[i] = new MapSqlParameterSource(bulkParamMap);
        }
        return sqlParameterSources;
    }


    /**
     * 获取插入新商品时的数组索引
     *
     * @param shopShelfProducts
     * @param start
     * @param end
     * @param insertLocation
     * @return
     */
    private int getIndexOfShelfProduct(List<ShopShelfProduct> shopShelfProducts, int start, int end, int insertLocation) {

        int mid = (end - start) / 2 + start;

        ShopShelfProduct keyShelfProduct = shopShelfProducts.get(mid);
        Integer keyLocation = keyShelfProduct.getLocation();
        if (keyLocation.compareTo(insertLocation) == 0) {
            return mid;
        }

        if (start >= end) {
            return -1;
        } else if (keyLocation.compareTo(insertLocation) < 0) {
            return getIndexOfShelfProduct(shopShelfProducts, mid + 1, end, insertLocation);
        } else if (keyLocation.compareTo(insertLocation) > 0) {
            return getIndexOfShelfProduct(shopShelfProducts, start, mid - 1, insertLocation);
        }

        return -1;
    }


}
