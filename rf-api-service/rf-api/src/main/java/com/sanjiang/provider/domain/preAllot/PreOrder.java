package com.sanjiang.provider.domain.preAllot;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 订货订单
 *created by wangpan on 2018/08/01
 */
@Data
@ToString
public class PreOrder implements Serializable {

    private String storeId;       //门店号

    private String otherStoreId; //对方门店号

    private String userId;       //操作员ID

    private List<PreOrderGood> goodList;   //订货商品


}
