package com.sanjiang.provider.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by byinbo on 2018/6/25.
 */
@Data
@ToString
public class Supplier implements Serializable{

    private String bmbh;

    private String bmmc;

}
