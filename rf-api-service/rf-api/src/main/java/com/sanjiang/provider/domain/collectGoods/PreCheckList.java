package com.sanjiang.provider.domain.collectGoods;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by byinbo on 2018/5/21.
 */
@Data
public class PreCheckList implements Serializable {


    /**
     * 出库号
     */
    private String cch;

    /**
     * 出库日期
     */
    private LocalDate ckrq;

    /**
     * 商场编号
     */
    private String scbh;

    /**
     * 品种数
     */
    private Integer pzs;

    /**
     * 单品数
     */
    private Integer dps;

    /**
     * 金额
     */
    private Double je;

    /**
     * 重量
     */
    private Double zl;
}
