package com.sanjiang.provider.model.ClosedAllot;

import com.sanjiang.provider.model.GoodsBaseModel;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by byinbo on 2018/7/8.
 */
@Data
@ToString
public class DbckAreaModel extends GoodsBaseModel implements Serializable{

    private Integer xh;

    private Double rksl;

    private Double rkje;
}
