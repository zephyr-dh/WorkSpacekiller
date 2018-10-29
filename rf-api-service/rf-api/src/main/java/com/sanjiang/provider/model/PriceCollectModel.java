package com.sanjiang.provider.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by byinbo on 2018/6/21.
 */
@Data
@ToString
public class PriceCollectModel extends GoodsBaseModel implements Serializable{

    private String scname;

    private String czy;

    private Double price;

    private Double cPrice;

    private String bz;

}
