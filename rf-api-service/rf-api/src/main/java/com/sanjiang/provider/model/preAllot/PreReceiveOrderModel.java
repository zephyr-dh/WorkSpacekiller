package com.sanjiang.provider.model.preAllot;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 订单模型---入库订单
 * created by wangpan on 2018/08/17
 */
@Data
@ToString
public class PreReceiveOrderModel implements Serializable {

    private String documentsId;   //单据号

    private String orderId;      //订单编号

    private String otherStoreName;  //对方门店简称--调出门店简称

    private String otherStoreID;   //对方门店编号--调出门店ID

    private String storeId;        //调出门店ID

    private String boxCode;        //箱码号

    private Integer kindCount;     //商品品种数量


}
