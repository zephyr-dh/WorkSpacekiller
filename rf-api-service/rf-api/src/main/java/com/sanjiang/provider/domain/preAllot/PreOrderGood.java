package com.sanjiang.provider.domain.preAllot;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 前置仓调拨订单中的商品
 * created by wangpan on 2018/08/02
 */
@Data
@ToString
public class PreOrderGood implements Serializable {

    private String goodId; //商品管理码

    private Integer count;   //商品数量
}
