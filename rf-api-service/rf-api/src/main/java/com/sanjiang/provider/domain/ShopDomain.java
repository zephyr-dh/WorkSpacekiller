package com.sanjiang.provider.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 门店模型
 *
 * @author kimiyu
 * @date 2018/4/16 11:35
 */
@Data
@ToString
public class ShopDomain implements Serializable {

    private String BMBH;

    private String BMMC;

    private String SID;

    private String IP;
}
