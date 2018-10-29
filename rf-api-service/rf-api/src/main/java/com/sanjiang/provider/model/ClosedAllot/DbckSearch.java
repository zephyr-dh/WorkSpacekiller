package com.sanjiang.provider.model.ClosedAllot;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by byinbo on 2018/7/6.
 */
@Data
@ToString
public class DbckSearch implements Serializable{

    private String scbh;

    private String bmbh;

    private Integer bh;

    private Integer xh;
}
