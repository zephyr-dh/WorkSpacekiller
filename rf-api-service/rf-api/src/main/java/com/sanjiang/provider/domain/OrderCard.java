package com.sanjiang.provider.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 订货卡信息
 *
 * @author kimiyu
 * @date 2018/4/23 11:35
 */
@Data
@ToString
public class OrderCard implements Serializable {

    private String kh;

    private String scsj;

    private String yhsl;

    private String zkr;

    private String scr;


}
