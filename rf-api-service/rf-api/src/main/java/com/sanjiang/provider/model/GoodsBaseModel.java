package com.sanjiang.provider.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by byinbo on 2018/7/8.
 */
@Data
@ToString
public class GoodsBaseModel implements Serializable{

    private String spmc;

    private String spbh;

    private String glbh;

    private String spzt;

    private Double spjj;
}
