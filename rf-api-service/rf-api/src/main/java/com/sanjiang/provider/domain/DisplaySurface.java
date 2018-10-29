package com.sanjiang.provider.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 陈列面模型
 *
 * @author kimiyu
 * @date 2018/4/23 10:20
 */
@Data
@ToString
public class DisplaySurface implements Serializable {

    private String hjbh;

    private String clm;

    private String dg;

    private String zs;

    private String hjm;


}
