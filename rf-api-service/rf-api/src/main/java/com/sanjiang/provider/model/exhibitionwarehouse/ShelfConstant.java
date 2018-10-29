package com.sanjiang.provider.model.exhibitionwarehouse;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 货架常量模型
 *
 * @author kimiyu
 * @date 2018/4/26 11:50
 */
@Data
@ToString
public class ShelfConstant<T> implements Serializable {

    private String code;

    private String value;

    private T data;
}
