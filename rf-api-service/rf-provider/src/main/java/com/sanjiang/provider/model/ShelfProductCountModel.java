package com.sanjiang.provider.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 货架商品、层数模型
 *
 * @author kimiyu
 * @date 2018/5/8 16:37
 */
@Data
@ToString
public class ShelfProductCountModel implements Serializable {


    private String shelfNumber;

    private Integer totalLayerNumber = 0;

    private Integer totalLocation = 0;
}
