package com.sanjiang.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 公共模型
 *
 * @author kimiyu
 * @date 2018/4/21 13:30
 */
@Data
public class BaseModel implements Serializable {

    private String shopId;

    private String workerId;

    private String deviceId;
}
