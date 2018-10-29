package com.sanjiang.provider.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 多条码商品
 *
 * @author kimiyu
 * @date 2018/4/19 15:59
 */
@Data
public class MutilSpProduct implements Serializable {

    private Integer plulx;

    private String spbh;

    private String dhfs;

    private String dhzt;

    private String shfs;

    private String spztmc;

    private String spcd;

    private String plu;

    private String spmc;

    private String jzsj;

    private String jzhyj;

    private String glbh;
}
