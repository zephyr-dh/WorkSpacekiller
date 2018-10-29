package com.sanjiang.provider.model.preAllot;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 调出订单详情---箱码对象
 * Created by wangpan on 2018/8/6.
 */
@Data
@ToString
public class PreBoxModel implements Serializable{

    private String documentsId;   //单据号---和箱码号是一一对应

    private String boxCode;     //箱码

    private String goodKind;   //商品品种/个

    private String type;       //类型

    private String otherStoreName;    //对方门店名称

    private String otherStoreId;  //对方门店ID


}
