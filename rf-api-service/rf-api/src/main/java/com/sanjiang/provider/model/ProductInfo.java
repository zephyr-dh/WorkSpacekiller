package com.sanjiang.provider.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by byinbo on 2018/6/25.
 */
@Data
@ToString
public class ProductInfo extends GoodsBaseModel implements Serializable{

    private Double spjj;

    private Double gg;

}
