package com.sanjiang.provider.model;

import com.sanjiang.model.BaseModel;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 商品查询模型
 *
 * @author kimiyu
 * @date 2018/4/21 13:32
 */
@Data
@ToString
public class ProductSearch extends BaseModel implements Serializable {

    /**
     * 商品条码
     */
    private String spbm;
}
