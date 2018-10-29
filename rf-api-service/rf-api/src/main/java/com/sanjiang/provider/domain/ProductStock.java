package com.sanjiang.provider.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品库存模型
 *
 * @author kimiyu
 * @date 2018/4/23 10:48
 */
@Data
public class ProductStock implements Serializable {

    private String kcsl;

    private String zjdh;

    private String dhrq;

    private String pszt;

    private String xssl;

    private String ddrsl;

    private String drxssl;


}
