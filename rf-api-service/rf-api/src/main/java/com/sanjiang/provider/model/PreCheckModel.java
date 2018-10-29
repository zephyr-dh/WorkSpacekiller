package com.sanjiang.provider.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * Created by byinbo on 2018/5/24.
 * 生成预检单上传数据model
 */
@Data
@ToString
public class PreCheckModel implements Serializable {

    private String uuid;

    private String scbh;

    private String bmbh;

    private String cch;

    private String clry;

}
