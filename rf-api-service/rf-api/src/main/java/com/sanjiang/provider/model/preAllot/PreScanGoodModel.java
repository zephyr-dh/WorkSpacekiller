package com.sanjiang.provider.model.preAllot;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 扫描商品详情
 * Created by wangpan on 2018/7/31.
 */
@Data
@ToString
public class PreScanGoodModel implements Serializable{

    private String goodId;   //管理码

    private String goodCode;   //商品条码

    private String goodName;   //商品名称

    private String orderSpecification;   //订货规格

    private Integer repertoryCount;   //库存数量

    private String goodStatus;   //商品状态

    private Integer numInWeek;   //商品周销

    private Integer numInMonth; //商品四周均销

    private String goodType;   //商品类型  1--标品；0--称重码

    private Integer orderCountNow;  //已订数量

    private Integer otherShopRepertoryCount;   //对方门店库存

}
