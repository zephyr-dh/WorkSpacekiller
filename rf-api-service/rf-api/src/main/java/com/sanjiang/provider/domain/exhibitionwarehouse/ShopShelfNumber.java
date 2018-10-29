package com.sanjiang.provider.domain.exhibitionwarehouse;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/**
 * 商场货架编号采集
 *
 * @author kimiyu
 * @date 2018/4/26 10:27
 */
@Data
public class ShopShelfNumber implements Serializable {

    private String shopId;

    /**
     * 货架编号
     */
    private String shelfNumber;

    /**
     * 货架区域
     */
    private String shelfArea;

    /**
     * 货架陈列区域
     */
    private String shelfDisplayType;

    /**
     * 总层数
     */
    private Integer layerNumber = 0;

    /**
     * 商品总数量
     */
    private Integer productNumber = 0;

    /**
     * 修改日期
     */
    private Timestamp operateDate;


    public String getOperateDate() {
        return null == operateDate ? null : operateDate.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


}
