package com.sanjiang.provider.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 商品模型
 *
 * @author kimiyu
 * @date 2018/4/16 15:05
 */
@Data
@ToString
public class ProductDomain implements Serializable {

    private String glbh;

    private String spmc;

    private String spbh;

    /**
     * 订货方式
     */
    private String dhfs;

    /**
     * 送货方式
     */
    private String shfs;

    private String spztmc;

    /**
     * 商品产地
     */
    private String spcd;

    private String plu;

    /**
     * 基准售价
     */
    private String jzsj;

    /**
     * 基准会员价
     */
    private String jzhyj;

    private Set<MutilSpProduct> mutilSpProducts;

    /**
     * 订货信息
     */
    private List<OrderCard> orderCards;

    /**
     * 商品库存
     */
    private List<ProductStock> productStocks;

    /**
     * 陈列面
     */
    private List<DisplaySurface> displaySurfaces;

    /**
     * 单品销售
     */
    private List<TimeProductSale> timeProductSales;


}
