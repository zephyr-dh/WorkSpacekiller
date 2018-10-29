package com.sanjiang.consumer.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author kimiyu
 * @date 2018/5/4 16:39
 */
@Data
@ToString
public class ShopShelfProductModel implements Serializable {

    /**
     * 门店ID
     */
    private String shopId;

    /**
     * 货架编号
     */
    private String shelfNumber;

    /**
     * 流水号列表
     */
    private List<Long> serialNumbers;
}
