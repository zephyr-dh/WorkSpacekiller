package com.sanjiang.consumer.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 货架列表展示
 *
 * @author kimiyu
 * @date 2018/5/8 16:30
 */
@Data
@ToString
public class ShelfModel implements Serializable {

    private String group;


}
