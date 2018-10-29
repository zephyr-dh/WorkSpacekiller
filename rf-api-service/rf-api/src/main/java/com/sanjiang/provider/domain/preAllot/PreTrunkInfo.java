package com.sanjiang.provider.domain.preAllot;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 装车箱码信息对象
 */
@Data
@ToString
public class PreTrunkInfo implements Serializable {

    private String documentsId;      //单据号

    private String otherStoreId;   //对方门店号

    private String boxCode;     //箱码号
}
