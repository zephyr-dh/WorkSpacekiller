package com.sanjiang.provider.domain.collectGoods;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by byinbo on 2018/5/22.
 */
@Data
public class GoodsDetail implements Serializable {

    /**
     * 大件单号
     */
    private String djdh;

    /**
     * 管理编号
     */
    private String glbh;

    /**
     * 商品编号
     */
    private String spbh;

    /**
     * 商品名称
     */
    private String spmc;

    /**
     * 订货规格
     */
    private String dhgg;

    /**
     * 应收数
     */
    private String yss;

    /**
     * 应收件数
     */
    private String ys;

//    /**
//     * 实收数量
//     */
//    private String sssl;
//
//    /**
//     * 差异数量
//     */
//    private String cysl;
//
//    /**
//     * 差异件数
//     */
//    private String cyjs;

    /**
     * 单据类型
     */
    private String djly;

    /**
     * 出库日期
     */
    private LocalDate ckrq;
}
