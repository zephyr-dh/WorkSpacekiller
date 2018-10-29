package com.sanjiang.provider.model.ClosedAllot;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by byinbo on 2018/7/6.
 */
@Data
@ToString
public class ClosedAllotModel implements Serializable{

    private String scbh;

    private String bmbh;

    private String glbh;

    private Double rksl;

    private Double rkje;

    private Integer bh;

    private Integer xh;

//    private Date lrrq;

//    private String zt;

    private String spbh;

//    private Integer djh;
}
