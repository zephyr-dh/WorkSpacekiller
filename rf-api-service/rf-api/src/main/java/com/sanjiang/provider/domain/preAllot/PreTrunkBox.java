package com.sanjiang.provider.domain.preAllot;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 装车箱码模型
 * created by wangpan on 2018/08/21
 */
@Data
@ToString
public class PreTrunkBox implements Serializable {

    private String uuid;       //uuid

    private String storeId;     //门店编号

    private String userId;      //操作员编号

    private List<PreTrunkInfo>  info;   //箱码list
}
