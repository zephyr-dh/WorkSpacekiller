package com.sanjiang.provider.model.preAllot;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 调出订单分拣商品详情---待分拣和某箱码中的
 * Created by wangpan on 2018/8/6.
 */
@Data
@ToString
public class PreOutGoodsModel implements Serializable{

    private String goodId;   //管理码

    private String orderId;   //订单号

    private String goodCode;   //商品条码

    private String goodName;   //商品名称

    private String goodSelfId;    //货架编号

    private Integer pickCount;     //待捡数量

    private Integer pickedCount;   //已捡数量

    private String boxCode;         //商品所在箱码

    private Integer countInBox;    //该箱码内的商品数量


}
