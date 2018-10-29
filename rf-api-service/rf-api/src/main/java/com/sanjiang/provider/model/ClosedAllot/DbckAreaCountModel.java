package com.sanjiang.provider.model.ClosedAllot;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by byinbo on 2018/7/8.
 */
@Data
@ToString
public class DbckAreaCountModel implements Serializable{

    private Integer bh;

    private Integer types;

    private Double rksl;

    private Double rkje;
}
