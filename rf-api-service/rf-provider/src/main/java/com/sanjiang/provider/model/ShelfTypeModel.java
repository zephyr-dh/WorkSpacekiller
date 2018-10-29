package com.sanjiang.provider.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 货架类型
 *
 * @author kimiyu
 * @date 2018/5/5 09:37
 */
@Data
@ToString
public class ShelfTypeModel implements Serializable {

    private String type;

    private String code;

    private String display;
}
