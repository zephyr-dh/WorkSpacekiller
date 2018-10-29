package com.sanjiang.provider.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 商品时段销售模型
 *
 * @author kimiyu
 * @date 2018/4/23 11:55
 */
@Data
@ToString
public class TimeProductSale implements Serializable {

    private String xsrq;

    private String spmc;

    private String xssl;

    private String xsje;

    private String skjbh;

    private String czybh;

    private String lsh;
}
