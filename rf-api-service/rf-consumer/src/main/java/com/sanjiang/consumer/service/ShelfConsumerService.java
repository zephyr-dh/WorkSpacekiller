package com.sanjiang.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sanjiang.constrants.DefaultValue;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.core.ResultCode;
import com.sanjiang.provider.domain.exhibitionwarehouse.ShopShelfNumber;
import com.sanjiang.provider.service.exhibitionwarehouse.ShopShelfNumberService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 货架消费者服务
 *
 * @author kimiyu
 * @date 2018/4/26 13:03
 */
@Service
public class ShelfConsumerService {

    @Reference(
            application = "${dubbo.application.id}",
            registry = "${dubbo.registry.id}",
//            url = "dubbo://193.0.1.116:20880",
            group = "shelf",
            version = "1.0.0",
            timeout = 3000
    )
    private ShopShelfNumberService shopShelfNumberService;

    /**
     * 获取货架参数列表
     *
     * @param shopId
     * @return
     */
    public ResponseMessage getConstatns(String shopId) {
        return shopShelfNumberService.getShelfConstants(shopId);
    }

    /**
     * 获取货架参数列表
     *
     * @param shopId
     * @return
     */
    public ResponseMessage getShelfs(String shopId) {
        return shopShelfNumberService.getShelfs(shopId);
    }

    /**
     * 保存货架信息
     *
     * @param shopShelfNumber
     * @return
     */
    public ResponseMessage saveShelfNumber(ShopShelfNumber shopShelfNumber) {
        if (null == shopShelfNumber) {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "没有需要保存的货架", null);
        }

        String shopId = shopShelfNumber.getShopId();
        String shelfNumber = shopShelfNumber.getShelfNumber();
        String shelfArea = shopShelfNumber.getShelfArea();
        String shelfDisplayType = shopShelfNumber.getShelfDisplayType();

        if (StringUtils.isEmpty(shopId) || StringUtils.isEmpty(shelfNumber)
                || StringUtils.isEmpty(shelfArea) || StringUtils.isEmpty(shelfDisplayType)) {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), DefaultValue.DEFAULT_ERROR_PARAM.value(), null);
        }

        return shopShelfNumberService.save(shopShelfNumber);
    }

    /**
     * 根据门店和货架号查询门店和货架信息
     *
     * @param shopId
     * @param shelfNumber
     * @return
     */
    public ResponseMessage getShelf(String shopId, String shelfNumber) {
        if (StringUtils.isEmpty(shopId) || StringUtils.isEmpty(shelfNumber)) {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), DefaultValue.DEFAULT_ERROR_PARAM.value(), null);
        }

        return shopShelfNumberService.findByShopIdAndShelfNumber(shopId, shelfNumber);
    }

    /**
     * 根据门店号和货架号清理货架信息
     *
     * @param shopId      门店号
     * @param shelfNumber 货架号
     * @return
     */
    public ResponseMessage cleanShelf(String shopId, String shelfNumber) {
        if (StringUtils.isEmpty(shopId) || StringUtils.isEmpty(shelfNumber)) {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), DefaultValue.DEFAULT_ERROR_PARAM.value(), null);
        }

        return shopShelfNumberService.cleanShelf(shopId, shelfNumber);

    }
}
