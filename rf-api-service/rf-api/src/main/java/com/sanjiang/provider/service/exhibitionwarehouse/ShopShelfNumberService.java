package com.sanjiang.provider.service.exhibitionwarehouse;

import com.sanjiang.core.ResponseMessage;
import com.sanjiang.provider.domain.exhibitionwarehouse.ShopShelfNumber;

/**
 * @author kimiyu
 * @date 2018/4/26 10:38
 */
public interface ShopShelfNumberService {

    /**
     * 保存商场货架
     *
     * @param shopShelfNumber
     */
    ResponseMessage save(ShopShelfNumber shopShelfNumber);

    /**
     * 根据商场编号和
     *
     * @param shopId
     * @param shelfNumber
     * @return
     */
    ResponseMessage<ShopShelfNumber> findByShopIdAndShelfNumber(String shopId, String shelfNumber);

    /**
     * 获取货架常量列表
     *
     * @param shopId
     * @return
     */
    ResponseMessage getShelfConstants(String shopId);

    /**
     * 获取货架列表
     *
     * @param shopId
     * @return
     */
    ResponseMessage getShelfs(String shopId);


    /**
     * 根据门店号和货架号清空信息
     *
     * @param shopId
     * @param shelfNumber
     * @return
     */
    ResponseMessage cleanShelf(String shopId, String shelfNumber);

    // TODO 根据货架号和门店号更新层数
}
