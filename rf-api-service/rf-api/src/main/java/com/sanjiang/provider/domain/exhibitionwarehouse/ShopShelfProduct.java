package com.sanjiang.provider.domain.exhibitionwarehouse;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 商场货架商品模型
 *
 * @author kimiyu
 * @date 2018/4/26 14:15
 */
@Data
@ToString
public class ShopShelfProduct implements Serializable {

    private String shopId;

    /**
     * 删除操作
     */
    private boolean deleteOperate = false;

    /**
     * 货架编号
     */
    private String shelfNumber;

    /**
     * 层号
     */
    private Integer layerNumber = 1;

    public String getLayerNumberStr() {
        if (null == layerNumber) {
            layerNumber = 1;
        }
        if (layerNumber.compareTo(10) < 0) {
            return String.format("0%s", layerNumber);
        }
        return layerNumber.toString();
    }

    /**
     * 位置
     */
    private Integer location;

    public String getLocationStr() {
        if (location != null && location.compareTo(10) < 0) {
            return String.format("0%s", location);
        }
        return location != null ? String.valueOf(location) : "";
    }


    private Timestamp operateDate;

    public LocalDateTime getOperateDate() {
        return null == operateDate ? null : operateDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 流水号
     */
    private Long serialNumber;

    /**
     * 管理编号
     */
    private String erpGoodsId;

    /**
     * 商品条码
     */
    private String barCode;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 会员价
     */
    private String memberPrice;


}
