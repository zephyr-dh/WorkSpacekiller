package com.sanjiang.provider.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * Created by byinbo on 2018/5/24.
 * 收货上传数据model
 */
@Data
@ToString
public class CollectGoodsModel implements Serializable {

    private String uuid;

    private String djdh;

    private String scbh;

    private String bmbh;

    private String glbh;

    private String sssl;

}
