package com.sanjiang.provider.model.preAllot;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 订单模型---历史订单和有效的调出订单
 * created by wangpan on 2018/07/31
 */
@Data
@ToString
public class PreOrdersModel implements Serializable {

    private String orderType;  //订单类型

    private String orderDate;    //订单日期

    private String orderId;    //订单编号

    private String goodName;   //商品名称

    private String goodId;     //商品编号

    private Integer count;     //商品数量

    private Integer kindCount;     //商品品种数量

    private String orderStatus;    //订单状态 ’SH’  待拣货，’DC’  拣货完成   ‘DR’  订单入库

    private String otherStoreName;  //对方门店简称

    private String  otherStoreID;   //对方门店编号

    public String setOrderStatus(String status){
        if("SH".equals(status)){
            return "待拣货";
        }else if("DC".equals(status)){
            return "拣货完成";
        }else if("DR".equals(status)){
            return "订单入库";
        }else{
            return status;
        }

    }

}
