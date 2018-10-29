package com.sanjiang.provider.domain.preAllot;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 封箱模型
 *created by wangpan on 2018/08/10
 */
@Data
@ToString
public class PreSealBox implements Serializable {

    private String uuid;         //ID

    private String storeId;       //门店号

    private String otherStoreId;  //对方门店号

    private String boxCode;       //箱码

    private String userId;        //操作员ID

    private String orderId;      //订单号

    private List<PreOrderGood> goodList;   //箱中商品信息


}
