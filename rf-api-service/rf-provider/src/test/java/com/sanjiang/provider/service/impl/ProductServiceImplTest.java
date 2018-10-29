package com.sanjiang.provider.service.impl;

import com.sanjiang.constrants.DefaultValue;
import com.sanjiang.provider.domain.*;
import com.sanjiang.provider.model.ProductSearch;
import com.sanjiang.provider.service.BaseTest;
import com.sanjiang.provider.util.BaseCustomDao;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ProductServiceImplTest extends BaseTest {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private BaseCustomDao baseCustomDao;

    private String shopId;

    private HikariDataSource hikariDataSource;

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Before
    public void beforeTest() {
        shopId = "00023";
        hikariDataSource = baseCustomDao.customDataSource(shopId);
        try {
            jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            baseCustomDao.execScbh(jdbcTemplate, shopId);
            // 校验是否为同一存储过程
            System.out.println(jdbcTemplate.queryForObject("select sys_context('userenv','CLIENT_IDENTIFIER') from dual", EmptySqlParameterSource.INSTANCE, String.class));

        } catch (SQLException ex) {
            System.out.println("SQL执行异常：" + ExceptionUtils.getMessage(ex));
        }

    }

    @Test
    public void getOne() {

        ProductSearch productSearch = new ProductSearch();
        productSearch.setShopId(shopId);
        productSearch.setWorkerId("10693");
        productSearch.setDeviceId(UUID.randomUUID().toString());
        productSearch.setSpbm("2186449000769");

        List<ProductDomain> productDomains = productService.getBySPBM(productSearch);
        if (CollectionUtils.isEmpty(productDomains)) {
            System.out.println("没有查到对应的商品");
        } else {
            for (ProductDomain productDomain : productDomains) {
                System.out.println(productDomain.toString());
            }
        }
    }

    @Test
    public void getDisplaySurfaces() {

        List<DisplaySurface> displaySurfaceSet = productService.getDisplaySurfaces(jdbcTemplate, shopId, "5411316", "10693", UUID.randomUUID().toString());
        if (CollectionUtils.isEmpty(displaySurfaceSet)) {
            System.out.println("没有陈列面");
        } else {
            System.out.println("陈列面数据：" + displaySurfaceSet.toString());
        }

    }

    @Test
    public void getProductStock() {

        List<ProductStock> productStockSet = productService.getProductStock(jdbcTemplate, shopId, "5411316", "10693", UUID.randomUUID().toString());
        if (CollectionUtils.isEmpty(productStockSet)) {
            System.out.println("没有库存");
        } else {
            System.out.println("库存数据：" + productStockSet.toString());
        }


    }


    @Test
    public void getOrderCards() {


        List<OrderCard> orderCardSet = productService.getOrderCards(jdbcTemplate, shopId, "5411316", "10693", UUID.randomUUID().toString());
        if (CollectionUtils.isEmpty(orderCardSet)) {
            System.out.println("没有订货卡信息");
        } else {
            System.out.println("订货卡信息数据：" + orderCardSet.toString());
        }

    }

    @Test
    public void getTimeProductSales() {
        Long startTime = System.currentTimeMillis();
        List<TimeProductSale> timeProductSales = productService.getTimeProductSales(jdbcTemplate, shopId, "5411316", "10693", UUID.randomUUID().toString());

        if (CollectionUtils.isEmpty(timeProductSales)) {
            log.info("单品没有销售记录");
        } else {
            log.info("销售查询消耗时间：{};单品销售记录: {};", System.currentTimeMillis() - startTime, timeProductSales.toString());
        }
    }

    @Test
    public void testAsync() {

        ExecutorService executor = Executors.newFixedThreadPool(10);

        CompletableFuture<List<ProductStock>> asyncProductStocks = CompletableFuture.supplyAsync(() ->
                productService.getProductStock(jdbcTemplate, shopId, "5411316", "10693", UUID.randomUUID().toString()), executor
        );
        CompletableFuture<List<TimeProductSale>> asyncTimeProductSales = CompletableFuture.supplyAsync(() ->
                productService.getTimeProductSales(jdbcTemplate, shopId, "5411316", "10693", UUID.randomUUID().toString()), executor
        );
        CompletableFuture<List<OrderCard>> asyncOrderCards = CompletableFuture.supplyAsync(() ->
                productService.getOrderCards(jdbcTemplate, shopId, "5411316", "10693", UUID.randomUUID().toString()), executor
        );
        CompletableFuture<List<DisplaySurface>> asyncDisplaySurfaces = CompletableFuture.supplyAsync(() ->
                productService.getDisplaySurfaces(jdbcTemplate, shopId, "5411316", "10693", UUID.randomUUID().toString()), executor
        );


        try {
            Long startTime = System.currentTimeMillis();
//            List<ProductStock> productStocks = asyncProductStocks
//                    .get();
//            List<TimeProductSale> timeProductSales = asyncTimeProductSales.get();
//            List<OrderCard> orderCards = asyncOrderCards.get();
//            List<DisplaySurface> displaySurfaces = asyncDisplaySurfaces.get();
            CompletableFuture.allOf(asyncDisplaySurfaces, asyncOrderCards, asyncProductStocks, asyncTimeProductSales).join();
            long asyncTimeResult = System.currentTimeMillis() - startTime;
            log.info("异步处理的结果时间：{}", asyncTimeResult);

            Long syncStartTime = System.currentTimeMillis();
            List<ProductStock> syncProductStocks = productService.getProductStock(jdbcTemplate, shopId, "5411316", "10693", UUID.randomUUID().toString());
            List<TimeProductSale> syncTimeProductSales = productService.getTimeProductSales(jdbcTemplate, shopId, "5411316", "10693", UUID.randomUUID().toString());
            List<OrderCard> syncOrderCards = productService.getOrderCards(jdbcTemplate, shopId, "5411316", "10693", UUID.randomUUID().toString());
            List<DisplaySurface> syncDisplaySurfaces = productService.getDisplaySurfaces(jdbcTemplate, shopId, "5411316", "10693", UUID.randomUUID().toString());
            long syncTimeResult = System.currentTimeMillis() - syncStartTime;

            log.info("同步处理的结果时间：{}", syncTimeResult);

            log.info("同步、异步处理结果：{}", asyncTimeResult > syncTimeResult);


        } catch (Exception ex) {
            System.out.println("系统异常处理：" + ExceptionUtils.getMessage(ex));
        }

    }

    @Test
    public void getBySPBMNew() {

        ProductSearch productSearch = new ProductSearch();
        productSearch.setShopId(shopId);
        productSearch.setWorkerId("10693");
        productSearch.setDeviceId(UUID.randomUUID().toString());
        productSearch.setSpbm("2186449000769");

        List<ProductDomain> productDomains = productService.getBySPBMNew(productSearch);
        if (CollectionUtils.isEmpty(productDomains)) {
            System.out.println("没有查到对应的商品");
        } else {
            for (ProductDomain productDomain : productDomains) {
                System.out.println(productDomain.toString());
            }
        }
    }

    @After
    public void afterTest() {
        if (!hikariDataSource.isClosed()) {
            System.out.println(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
            hikariDataSource.close();
        }
    }


}