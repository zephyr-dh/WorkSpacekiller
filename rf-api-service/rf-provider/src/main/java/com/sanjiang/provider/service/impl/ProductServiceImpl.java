package com.sanjiang.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.sanjiang.constrants.DefaultValue;
import com.sanjiang.provider.constrants.ProcType;
import com.sanjiang.provider.constrants.SqlParam;
import com.sanjiang.provider.domain.*;
import com.sanjiang.provider.model.ProductSearch;
import com.sanjiang.provider.service.ProductService;
import com.sanjiang.provider.util.BaseCustomDao;
import com.sanjiang.provider.util.ProductUtil;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleTypes;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * 商品服务
 *
 * @author kimiyu
 * @date 2018/4/17 10:12
 */
@Service(
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}",
        group = "product",
        version = "1.0.1",
        timeout = 50000
)
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private BaseCustomDao baseCustomDao;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * 1. 根据门店ID和sp编码返回结果集
     *
     * @param productSearch 商品查询模型
     * @return
     */
    @Override
    public List<ProductDomain> getBySPBM(ProductSearch productSearch) {

        log.info("rf枪商品查询：{}", productSearch.toString());

        String shopId = productSearch.getShopId();
        String workerId = productSearch.getWorkerId();
        String spbm = productSearch.getSpbm();
        String deviceId = productSearch.getDeviceId();

        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(shopId);

        try {
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            // 执行存储过程
            baseCustomDao.execScbh(jdbcTemplate, shopId);

            List resultList = (List) jdbcTemplate.getJdbcOperations()
                    .execute(genShopProc(ProcType.LAST_SIX_BARCODE_SEARCH.value(),
                            shopId, spbm, workerId, deviceId), genTm6cxCallback());

            if (CollectionUtils.isEmpty(resultList)) {
                return new ArrayList<>();
            }

            List<ProductDomain> productDomains = new ArrayList<>();
            for (Object o : resultList) {
                Map rowMap = (Map) o;
                ProductDomain productDomain = new ProductDomain();
                productDomain.setGlbh(rowMap.get("glbh").toString());
                productDomain.setSpmc(rowMap.get("spmc").toString());
                productDomain.setSpbh(rowMap.get("spbh").toString());
                productDomains.add(productDomain);
            }

            int size = productDomains.size();
            if (size > 1) {
                return productDomains;
            }

            ProductDomain singleProductDomain = productDomains.get(0);
            String erpGoodsId = singleProductDomain.getGlbh();

            // 处理库存
            List<ProductStock> productStocks = getProductStock(jdbcTemplate, shopId, erpGoodsId, workerId, deviceId);
            // 处理陈列
            List<DisplaySurface> displaySurfaces = getDisplaySurfaces(jdbcTemplate, shopId, erpGoodsId, workerId, deviceId);
            // 处理销售
            List<TimeProductSale> timeProductSales = getTimeProductSales(jdbcTemplate, shopId, erpGoodsId, workerId, deviceId);
            // 处理订货信息
            List<OrderCard> orderCards = getOrderCards(jdbcTemplate, shopId, erpGoodsId, workerId, deviceId);
            boolean freshItem = ProductUtil.checkFreshSPBM(spbm);
            String type = freshItem ? ProcType.FRESH_PPRODUCT.value() : ProcType.NOT_FRESH_PRODUCT.value();
            if (freshItem) {
                log.info("单个生鲜商品：{}", erpGoodsId);
                List sxspList = (List) jdbcTemplate.getJdbcOperations().execute(genShopProc(type, shopId, erpGoodsId, workerId, deviceId), genSxspCallback());

                if (CollectionUtils.isEmpty(sxspList)) {
                    return productDomains;
                }

                Set<MutilSpProduct> mutilSpProducts = getMutilSpProducts(sxspList, type);

                MutilSpProduct minMutilSpProduct = mutilSpProducts.stream().min(Comparator.comparing(MutilSpProduct::getPlulx)).orElse(null);
                if (minMutilSpProduct != null) {
                    singleProductDomain = modelMapper.map(minMutilSpProduct, ProductDomain.class);
                }
                singleProductDomain.setMutilSpProducts(mutilSpProducts);
            } else {
                // 非生鲜
                List qtmcxList = (List) jdbcTemplate.getJdbcOperations().execute(genShopProc(type, shopId, spbm, workerId, deviceId), genGtmcxCallback());

                if (CollectionUtils.isEmpty(qtmcxList)) {
                    return productDomains;
                }

                Set<MutilSpProduct> mutilSpProducts = getMutilSpProducts(qtmcxList, type);
                MutilSpProduct minMutilSpProduct = mutilSpProducts.stream().findFirst().orElse(null);
                if (minMutilSpProduct != null) {
                    singleProductDomain = modelMapper.map(minMutilSpProduct, ProductDomain.class);
                }
                singleProductDomain.setMutilSpProducts(mutilSpProducts);
            }

            singleProductDomain.setProductStocks(productStocks);
            singleProductDomain.setDisplaySurfaces(displaySurfaces);
            singleProductDomain.setOrderCards(orderCards);
            singleProductDomain.setTimeProductSales(timeProductSales);
            productDomains.set(0, singleProductDomain);

            return productDomains;

        } catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<ProductDomain> getBySPBMNew(ProductSearch productSearch) {
        log.info("rf枪商品查询：{}", productSearch.toString());

        String shopId = productSearch.getShopId();
        String workerId = productSearch.getWorkerId();
        String spbm = productSearch.getSpbm();
        String deviceId = productSearch.getDeviceId();
        HikariDataSource hikariDataSource = baseCustomDao.customDataSource(shopId);
        try {
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            // 执行存储过程
            baseCustomDao.execScbh(jdbcTemplate, shopId);

            List sxspList = (List) jdbcTemplate.getJdbcOperations()
                    .execute(genShopProc(ProcType.BARCODE_SEARCH.value(), shopId, spbm, workerId, deviceId),
                            genBarcodeSeachCallback());

            if (CollectionUtils.isEmpty(sxspList)) {
                return new ArrayList<>();
            }

            ProductDomain singleProductDomain = new ProductDomain();
            Set<MutilSpProduct> mutilSpProducts = getMutilSpProducts(sxspList, ProcType.BARCODE_SEARCH.value());
            if (!CollectionUtils.isEmpty(mutilSpProducts) && mutilSpProducts.size() == 1) {
                MutilSpProduct minMutilSpProduct = mutilSpProducts.stream().findFirst().orElse(null);
                singleProductDomain = modelMapper.map(minMutilSpProduct, ProductDomain.class);
            } else {
                singleProductDomain.setMutilSpProducts(mutilSpProducts);
            }

            List<ProductDomain> productDomains = new ArrayList<>();
            productDomains.add(singleProductDomain);
            return productDomains;
        } catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
        return new ArrayList<>();
    }

    /**
     * 处理多条码商品列表
     *
     * @param productList
     * @param type
     * @return
     */
    private Set<MutilSpProduct> getMutilSpProducts(List productList, String type) {
        Set<MutilSpProduct> mutilSpProducts = new HashSet<>();
        for (Object o : productList) {
            Map rowMap = (Map) o;
            MutilSpProduct mutilSpProduct = new MutilSpProduct();
            mutilSpProduct.setDhfs(rowMap.get("dhfs").toString());
            mutilSpProduct.setDhzt(rowMap.get("dhzt").toString());
            String memberPrice = rowMap.get(SqlParam.AS_JZHYJ.value()).toString();
            if (!StringUtils.isEmpty(memberPrice)) {
                memberPrice = new BigDecimal(memberPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            }
            mutilSpProduct.setJzhyj(memberPrice);
            String sellPrice = rowMap.get("jzsj").toString();
            if (!StringUtils.isEmpty(sellPrice)) {
                sellPrice = new BigDecimal(sellPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            }
            mutilSpProduct.setJzsj(sellPrice);
            mutilSpProduct.setShfs(rowMap.get("shfs").toString());
            mutilSpProduct.setSpztmc(rowMap.get("spztmc").toString());
            mutilSpProduct.setSpbh(rowMap.get("spbh").toString());
            mutilSpProduct.setSpcd(rowMap.get("spcd").toString());
            mutilSpProduct.setSpmc(rowMap.get("spmc").toString());
            mutilSpProduct.setGlbh(rowMap.get("glbh").toString());
            if (ProcType.FRESH_PPRODUCT.value().equalsIgnoreCase(type)) {
                mutilSpProduct.setPlulx(Integer.valueOf(rowMap.get("plulx").toString()));
                mutilSpProduct.setPlu(rowMap.get("plu").toString());
            }
            mutilSpProducts.add(mutilSpProduct);
        }
        return mutilSpProducts;
    }

    /**
     * 保存商品查询日志执行存储过程
     *
     * @param shopId 门店编号
     * @return
     * @deprecated 由存储过程去处理日志
     */
    @Deprecated()
    public void genLogProc(Connection connection, String type, String shopId, String erpGoodsId, String workerId, String rfMachineId) throws SQLException {
        String sql = "{call pkg_cx_rf.p_rcz(?,?,?,?,?)}";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, shopId);
            preparedStatement.setString(3, erpGoodsId);
            preparedStatement.setString(4, workerId);
            preparedStatement.setString(5, rfMachineId);
            preparedStatement.execute();
        }
    }


    /**
     * 根据类型执行存储过程
     *
     * @param type
     * @param shopId
     * @param erpGoodsId
     * @return
     */
    private CallableStatementCreator genShopProc(String type,
                                                 String shopId,
                                                 String erpGoodsId,
                                                 String workerId,
                                                 String deviceId) {
        return con -> {

            String storedProc = "{call pkg_cx_rf.p_zlcx(?,?,?,?,?,?)}";
            CallableStatement callableStatement = con.prepareCall(storedProc);
            callableStatement.setString(1, type);
            callableStatement.setString(2, shopId);
            callableStatement.setString(3, erpGoodsId);
            callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
            callableStatement.setString(5, workerId);
            callableStatement.setString(6, deviceId);
            return callableStatement;
        };
    }

    /**
     * tm6cx存储过程
     *
     * @return
     */
    private CallableStatementCallback genTm6cxCallback() {
        return cs -> {
            List resultMap = new ArrayList();
            cs.execute();
            ResultSet rs = (ResultSet) cs.getObject(4);
            while (rs.next()) {
                Map rowMap = new HashMap();
                rowMap.put("glbh", rs.getString("glbh"));
                rowMap.put("spmc", rs.getString("spmc"));
                rowMap.put("spbh", rs.getString("spbh"));
                resultMap.add(rowMap);
            }
            rs.close();
            return resultMap;
        };
    }

    /**
     * Sxsp存储过程
     *
     * @return
     */
    private CallableStatementCallback genBarcodeSeachCallback() {
        return cs -> {
            List resultMap = new ArrayList();
            cs.execute();
            ResultSet rs = (ResultSet) cs.getObject(4);
            while (rs.next()) {
                Map rowMap = new HashMap();
                rowMap.put("spbh", rs.getString("spbh"));
                rowMap.put("dhzt", rs.getString("dhzt"));
                rowMap.put("dhfs", rs.getString("dhfs"));
                rowMap.put("shfs", rs.getString("shfs"));
                rowMap.put("spztmc", rs.getString("spztmc"));
                rowMap.put("glbh", rs.getString("glbh"));
                rowMap.put("scbh", rs.getString("scbh"));
                rowMap.put("spcd", rs.getString("spcd"));
                rowMap.put("spmc", rs.getString("spmc"));
                rowMap.put("jzsj", rs.getString("jzsj"));
                rowMap.put(SqlParam.AS_JZHYJ.value(), rs.getString(SqlParam.AS_JZHYJ.value()));
                resultMap.add(rowMap);
            }
            rs.close();
            return resultMap;
        };
    }

    /**
     * Sxsp存储过程
     *
     * @return
     */
    private CallableStatementCallback genSxspCallback() {
        return cs -> {
            List resultMap = new ArrayList();
            cs.execute();
            ResultSet rs = (ResultSet) cs.getObject(4);
            while (rs.next()) {
                Map rowMap = new HashMap();
                rowMap.put("spbh", rs.getString("spbh"));
                rowMap.put("dhzt", rs.getString("dhzt"));
                rowMap.put("dhfs", rs.getString("dhfs"));
                rowMap.put("shfs", rs.getString("shfs"));
                rowMap.put("spztmc", rs.getString("spztmc"));
                rowMap.put("glbh", rs.getString("glbh"));
                rowMap.put("scbh", rs.getString("scbh"));
                rowMap.put("spcd", rs.getString("spcd"));
                rowMap.put("plu", rs.getString("plu"));
                rowMap.put("spmc", rs.getString("spmc"));
                rowMap.put("jzsj", rs.getString("jzsj"));
                rowMap.put(SqlParam.AS_JZHYJ.value(), rs.getString(SqlParam.AS_JZHYJ.value()));
                rowMap.put("plulx", rs.getInt("plulx"));
                resultMap.add(rowMap);
            }
            rs.close();
            return resultMap;
        };
    }

    /**
     * qtmcx存储过程
     *
     * @return
     */
    private CallableStatementCallback genGtmcxCallback() {
        return cs -> {
            List resultMap = new ArrayList();
            cs.execute();
            ResultSet rs = (ResultSet) cs.getObject(4);
            while (rs.next()) {
                Map rowMap = new HashMap();
                rowMap.put("spbh", rs.getString("spbh"));
                rowMap.put("dhzt", rs.getString("dhzt"));
                rowMap.put("dhfs", rs.getString("dhfs"));
                rowMap.put("shfs", rs.getString("shfs"));
                rowMap.put("spztmc", rs.getString("spztmc"));
                rowMap.put("glbh", rs.getString("glbh"));
                rowMap.put("scbh", rs.getString("scbh"));
                rowMap.put("spcd", rs.getString("spcd"));
                rowMap.put("spmc", rs.getString("spmc"));
                rowMap.put("jzsj", rs.getString("jzsj"));
                rowMap.put(SqlParam.AS_JZHYJ.value(), rs.getString(SqlParam.AS_JZHYJ.value()));
                resultMap.add(rowMap);
            }
            rs.close();
            return resultMap;
        };
    }

    /**
     * 获取陈列列表信息
     *
     * @param jdbcTemplate
     * @param shopId
     * @param erpGoodsId
     * @param workerId
     * @param deviceId
     * @return
     * @throws SQLException
     */
    public List<DisplaySurface> getDisplaySurfaces(NamedParameterJdbcTemplate jdbcTemplate,
                                                   String shopId,
                                                   String erpGoodsId,
                                                   String workerId,
                                                   String deviceId) {
        String type = ProcType.DISPLAY_SURFACE.value();
        //
        List clmList = (List) jdbcTemplate.getJdbcOperations().execute(genShopProc(type, shopId, erpGoodsId, workerId, deviceId), genClmCallback());

        if (CollectionUtils.isEmpty(clmList)) {
            return new ArrayList<>();
        }

        List<DisplaySurface> displaySurfaceSet = new ArrayList<>();
        for (Object o : clmList) {
            Map rowMap = (Map) o;
            DisplaySurface displaySurface = new DisplaySurface();
            displaySurface.setClm(rowMap.get("clm").toString());
            displaySurface.setDg(rowMap.get("dg").toString());
            displaySurface.setHjbh(rowMap.get("hjbh").toString());
            displaySurface.setHjm(rowMap.get("hjm").toString());
            displaySurface.setZs(rowMap.get("zs").toString());

            displaySurfaceSet.add(displaySurface);
        }
        return displaySurfaceSet;

    }


    /**
     * 陈列面
     *
     * @return
     */
    private CallableStatementCallback genClmCallback() {
        return cs -> {
            List resultMap = new ArrayList();
            cs.execute();
            ResultSet rs = (ResultSet) cs.getObject(4);
            while (rs.next()) {
                Map rowMap = new HashMap();
                rowMap.put("hjbh", rs.getString("hjbh"));
                rowMap.put("clm", rs.getString("clm"));
                rowMap.put("dg", rs.getString("dg"));
                rowMap.put("zs", rs.getString("zs"));
                rowMap.put("hjm", rs.getString("hjm"));
                resultMap.add(rowMap);
            }
            rs.close();
            return resultMap;
        };
    }

    /**
     * 根据管理编码获取门店库存信息
     *
     * @param jdbcTemplate
     * @param shopId
     * @param erpGoodsId
     * @return
     * @throws SQLException
     */
    public List<ProductStock> getProductStock(NamedParameterJdbcTemplate jdbcTemplate,
                                              String shopId,
                                              String erpGoodsId,
                                              String workerId,
                                              String deviceId
    ) {

        String type = ProcType.SEARCH_STOCK.value();
        //
        List stockList = (List) jdbcTemplate.getJdbcOperations().execute(genShopProc(type, shopId, erpGoodsId, workerId, deviceId), genKcbCallback());

        if (CollectionUtils.isEmpty(stockList)) {
            return new ArrayList<>();
        }

        List<ProductStock> productStockSet = new ArrayList<>();
        for (Object o : stockList) {
            Map rowMap = (Map) o;
            ProductStock productStock = new ProductStock();
            Object ddrsl = rowMap.get("ddrsl");
            productStock.setDdrsl(null == ddrsl ? "" : ddrsl.toString());
            Object dhrq = rowMap.get("dhrq");
            productStock.setDhrq(null == dhrq ? "" : dhrq.toString());
            Object drxssl = rowMap.get("drxssl");
            productStock.setDrxssl(null == drxssl ? "" : drxssl.toString());
            productStock.setKcsl(rowMap.get("kcsl").toString());
            productStock.setPszt(rowMap.get("pszt").toString());
            productStock.setXssl(rowMap.get("xssl").toString());
            productStock.setZjdh(rowMap.get("zjdh").toString());
            productStockSet.add(productStock);
        }
        return productStockSet;

    }


    /**
     * 库存
     *
     * @return
     */
    private CallableStatementCallback genKcbCallback() {
        return cs -> {
            List resultMap = new ArrayList();
            cs.execute();
            ResultSet rs = (ResultSet) cs.getObject(4);
            while (rs.next()) {
                Map rowMap = new HashMap();
                rowMap.put("ddrsl", rs.getString("ddrsl"));
                rowMap.put("dhrq", rs.getString("dhrq"));
                rowMap.put("drxssl", rs.getString("drxssl"));
                rowMap.put("kcsl", rs.getString("kcsl"));
                rowMap.put("pszt", rs.getString("pszt"));
                rowMap.put("xssl", rs.getString("xssl"));
                rowMap.put("zjdh", rs.getString("zjdh"));
                resultMap.add(rowMap);
            }
            rs.close();
            return resultMap;
        };
    }


    /**
     * 根据订货卡信息
     *
     * @param jdbcTemplate
     * @param shopId
     * @param erpGoodsId
     * @param workerId
     * @param deviceId
     * @return
     * @throws SQLException
     */
    public List<OrderCard> getOrderCards(NamedParameterJdbcTemplate jdbcTemplate,
                                         String shopId,
                                         String erpGoodsId,
                                         String workerId,
                                         String deviceId) {

        String type = ProcType.ORDER_CARD.value();
        //
        List dhkList = (List) jdbcTemplate.getJdbcOperations().execute(genShopProc(type, shopId, erpGoodsId, workerId, deviceId), genDhkCallback());

        if (CollectionUtils.isEmpty(dhkList)) {
            return new ArrayList<>();
        }

        List<OrderCard> orderCards = new ArrayList<>();
        for (Object o : dhkList) {
            Map rowMap = (Map) o;
            OrderCard orderCard = new OrderCard();
            orderCard.setKh(rowMap.get("kh").toString());
            orderCard.setScsj(rowMap.get("scsj").toString());
            orderCard.setYhsl(rowMap.get("yhsl").toString());
            orderCard.setZkr(rowMap.get("zkr").toString());
            orderCard.setScr(rowMap.get("scr").toString());
            orderCards.add(orderCard);
        }
        return orderCards;

    }


    /**
     * 订货卡
     *
     * @return
     */
    private CallableStatementCallback genDhkCallback() {
        return cs -> {
            List resultMap = new ArrayList();
            cs.execute();
            ResultSet rs = (ResultSet) cs.getObject(4);
            while (rs.next()) {
                Map rowMap = new HashMap();
                rowMap.put("kh", rs.getString("kh"));
                rowMap.put("scsj", rs.getString("scsj"));
                rowMap.put("yhsl", rs.getString("yhsl"));
                rowMap.put("zkr", rs.getString("zkr"));
                rowMap.put("scr", rs.getString("scr"));
                resultMap.add(rowMap);
            }
            rs.close();
            return resultMap;
        };
    }


    /**
     * 获取商品销售信息
     *
     * @param jdbcTemplate
     * @param shopId
     * @param erpGoodsId
     * @param workerId
     * @param deviceId
     * @return
     * @throws SQLException
     */
    public List<TimeProductSale> getTimeProductSales(NamedParameterJdbcTemplate jdbcTemplate,
                                                     String shopId,
                                                     String erpGoodsId,
                                                     String workerId,
                                                     String deviceId) {

        String type = ProcType.SINGLE_PRODUCT_SALE.value();
        //
        List drsplsList = (List) jdbcTemplate.getJdbcOperations().execute(genShopProc(type, shopId, erpGoodsId, workerId, deviceId), genDrsplsCallback());

        if (CollectionUtils.isEmpty(drsplsList)) {
            return new ArrayList<>();
        }

        List<TimeProductSale> timeProductSales = new ArrayList<>();
        for (Object o : drsplsList) {
            Map rowMap = (Map) o;
            TimeProductSale timeProductSale = new TimeProductSale();
            timeProductSale.setXsrq(rowMap.get("xsrq").toString());
            timeProductSale.setCzybh(rowMap.get("czybh").toString());
            timeProductSale.setLsh(rowMap.get("lsh").toString());
            timeProductSale.setSkjbh(rowMap.get("skjbh").toString());
            timeProductSale.setSpmc(rowMap.get("spmc").toString());
            timeProductSale.setXsje(rowMap.get("xsje").toString());
            timeProductSale.setXssl(rowMap.get("xssl").toString());
            timeProductSales.add(timeProductSale);
        }
        return timeProductSales;

    }


    /**
     * 单个商品销售
     *
     * @return
     */
    private CallableStatementCallback genDrsplsCallback() {
        return cs -> {
            List resultMap = new ArrayList();
            cs.execute();
            ResultSet rs = (ResultSet) cs.getObject(4);
            while (rs.next()) {
                Map rowMap = new HashMap();
                rowMap.put("xsrq", rs.getString("xsrq"));
                rowMap.put("spmc", rs.getString("spmc"));
                rowMap.put("xssl", rs.getString("xssl"));
                rowMap.put("xsje", rs.getString("xsje"));
                rowMap.put("skjbh", rs.getString("skjbh"));
                rowMap.put("czybh", rs.getString("czybh"));
                rowMap.put("lsh", rs.getString("lsh"));
                resultMap.add(rowMap);
            }
            rs.close();
            return resultMap;
        };
    }

}
