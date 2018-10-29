package com.sanjiang.provider.service.exhibitionwarehouse;

import com.sanjiang.core.ResponseMessage;
import com.sanjiang.provider.domain.exhibitionwarehouse.ShopShelfProduct;

import java.util.List;

/**
 * 货架商品服务
 *
 * @author kimiyu
 * @date 2018/4/26 18:34
 */
public interface ShopShelfProductService {


    /**
     * 保存货架商品信息
     *
     * @param shopShelfProduct
     * @return
     */
    ResponseMessage save(ShopShelfProduct shopShelfProduct);

    /**
     * 根据门店编号、货架好、层号展示当前层的商品列表
     *
     * @param shopId
     * @param shelfNumber
     * @param layerNumber
     * @param operateDelete
     * @return
     */
    ResponseMessage<List<ShopShelfProduct>> list(String shopId, String shelfNumber, Integer layerNumber, boolean operateDelete);

    /**
     * 按层号删除商品
     *
     * @param shopId      门店编号
     * @param shelfNumber 货架编号
     * @param layerNumber 层号
     * @return
     */
    ResponseMessage deleteByLayerNumber(String shopId, String shelfNumber, Integer layerNumber);


    /**
     * 按流水号删除商品
     *
     * @param shopId
     * @param shelfNumber
     * @param serialNumber
     * @return
     */
    ResponseMessage deleteById(String shopId, String shelfNumber, Long serialNumber);

    /**
     * 按流水号列表删除商品信息
     *
     * @param shopId
     * @param shelfNumber
     * @param serialNumbers
     * @return
     */
    ResponseMessage deleteByIdList(String shopId, String shelfNumber, List<Long> serialNumbers);


    /**
     * 根据门店号和管理编号获取对应的货架商品位置
     *
     * @param shopId
     * @param shelfNumber
     * @param erpGoodsId
     * @return
     */
    ResponseMessage<List<ShopShelfProduct>> getLocationList(String shopId, String shelfNumber, String erpGoodsId);


}
