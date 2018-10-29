package com.sanjiang.provider.constrants;

/**
 * RF枪存储过程类型
 *
 * @author kimiyu
 * @date 2018/4/23 08:43
 */
public enum ProcType {

    /**
     * 根据条码后六位查询
     */
    LAST_SIX_BARCODE_SEARCH("tm6cx"),

    /**
     * 订货卡查询
     */
    ORDER_CARD("dhk"),

    /**
     * 查询单个商品销售
     */
    SINGLE_PRODUCT_SALE("drspls"),

    /**
     * 商品称重、金额
     */
    SEARCH_PLU("plu"),

    /**
     * 根据管理编码查询生鲜商品明细
     */
    FRESH_PPRODUCT("sxsp"),

    /**
     * 陈列面
     */
    DISPLAY_SURFACE("clm"),

    /**
     * 查询库存
     */
    SEARCH_STOCK("kcb"),

    /**
     * 条码查询
     */
    BARCODE_SEARCH("tmcx"),


    /**
     * 根据条码查询非生鲜商品明细
     */
    NOT_FRESH_PRODUCT("qtmcx");


    private final String value;

    ProcType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
