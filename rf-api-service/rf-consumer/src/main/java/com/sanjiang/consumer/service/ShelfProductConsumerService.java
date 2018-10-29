package com.sanjiang.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sanjiang.constrants.DefaultValue;
import com.sanjiang.consumer.model.ShopShelfProductModel;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.core.ResultCode;
import com.sanjiang.provider.domain.exhibitionwarehouse.ShopShelfProduct;
import com.sanjiang.provider.service.exhibitionwarehouse.ShopShelfProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 货架商品服务
 *
 * @author kimiyu
 * @date 2018/5/2 10:08
 */
@Service
public class ShelfProductConsumerService {

    @Reference(
            application = "${dubbo.application.id}",
            registry = "${dubbo.registry.id}",
//            url = "dubbo://193.0.1.116:20880",
            group = "shelf-product",
            version = "1.0.0",
            timeout = 3000
    )
    private ShopShelfProductService shopShelfProductService;

    /**
     * 保存货架商品
     *
     * @param shopShelfProduct
     * @return
     */
    public ResponseMessage save(ShopShelfProduct shopShelfProduct) {

        if (null == shopShelfProduct) {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), DefaultValue.DEFAULT_ERROR_PARAM.value(), null);
        }

        String shopId = shopShelfProduct.getShopId();
        if (StringUtils.isEmpty(shopId)) {
            shopId = DefaultValue.DEFAULT_SHOPID.value();
        }
        String erpGoodsId = shopShelfProduct.getErpGoodsId();
        String shelfNumber = shopShelfProduct.getShelfNumber();
        String barCode = shopShelfProduct.getBarCode();

        if (StringUtils.isEmpty(erpGoodsId) || StringUtils.isEmpty(shelfNumber) || StringUtils.isEmpty(barCode)) {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), DefaultValue.DEFAULT_ERROR_PARAM.value(), null);
        }

        shopShelfProduct.setShopId(shopId);

        return shopShelfProductService.save(shopShelfProduct);
    }

    /**
     * 货架商品列表
     *
     * @param shopShelfProduct
     * @return
     */
    public ResponseMessage list(ShopShelfProduct shopShelfProduct) {

        if (null == shopShelfProduct) {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), DefaultValue.DEFAULT_ERROR_PARAM.value(), null);
        }

        String shopId = shopShelfProduct.getShopId();
        if (StringUtils.isEmpty(shopId)) {
            shopId = DefaultValue.DEFAULT_SHOPID.value();
        }
        String shelfNumber = shopShelfProduct.getShelfNumber();
        Integer layerNumber = shopShelfProduct.getLayerNumber();

        if (StringUtils.isEmpty(shelfNumber) || null == layerNumber) {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), DefaultValue.DEFAULT_ERROR_PARAM.value(), null);
        }

        return shopShelfProductService.list(shopId, shelfNumber, layerNumber, false);
    }

    /**
     * 根据层号删除商品
     *
     * @param shopShelfProduct
     * @return
     */
    public ResponseMessage delProductByLayerNumber(ShopShelfProduct shopShelfProduct) {
        if (null == shopShelfProduct) {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), DefaultValue.DEFAULT_ERROR_PARAM.value(), null);
        }

        String shopId = shopShelfProduct.getShopId();
        if (StringUtils.isEmpty(shopId)) {
            shopId = DefaultValue.DEFAULT_SHOPID.value();
        }
        String shelfNumber = shopShelfProduct.getShelfNumber();
        Integer layerNumber = shopShelfProduct.getLayerNumber();

        if (StringUtils.isEmpty(shopId) || StringUtils.isEmpty(shelfNumber) || null == layerNumber) {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), DefaultValue.DEFAULT_ERROR_PARAM.value(), null);
        }

        return shopShelfProductService.deleteByLayerNumber(shopId, shelfNumber, layerNumber);
    }


    /**
     * 根据序列号删除商品
     *
     * @param shopShelfProduct
     * @return
     */
    public ResponseMessage delProductById(ShopShelfProduct shopShelfProduct) {
        if (null == shopShelfProduct) {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), DefaultValue.DEFAULT_ERROR_PARAM.value(), null);
        }

        String shopId = shopShelfProduct.getShopId();
        if (StringUtils.isEmpty(shopId)) {
            shopId = DefaultValue.DEFAULT_SHOPID.value();
        }
        String shelfNumber = shopShelfProduct.getShelfNumber();
        Long serialNumber = shopShelfProduct.getSerialNumber();

        if (StringUtils.isEmpty(shopId) || StringUtils.isEmpty(shelfNumber) || null == serialNumber) {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), DefaultValue.DEFAULT_ERROR_PARAM.value(), null);
        }

        return shopShelfProductService.deleteById(shopId, shelfNumber, serialNumber);
    }

    /**
     * 根据序列号删除商品
     *
     * @param shopShelfProductModel
     * @return
     */
    public ResponseMessage delProductByIdList(ShopShelfProductModel shopShelfProductModel) {
        if (null == shopShelfProductModel) {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), DefaultValue.DEFAULT_ERROR_PARAM.value(), null);
        }

        String shopId = shopShelfProductModel.getShopId();
        if (StringUtils.isEmpty(shopId)) {
            shopId = DefaultValue.DEFAULT_SHOPID.value();
        }
        String shelfNumber = shopShelfProductModel.getShelfNumber();
        List<Long> serialNumbers = shopShelfProductModel.getSerialNumbers();

        if (StringUtils.isEmpty(shopId) || StringUtils.isEmpty(shelfNumber) || CollectionUtils.isEmpty(serialNumbers)) {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), DefaultValue.DEFAULT_ERROR_PARAM.value(), null);
        }

        return shopShelfProductService.deleteByIdList(shopId, shelfNumber, serialNumbers);
    }


    public ResponseMessage getByLocation(ShopShelfProduct shopShelfProduct) {
        if (null == shopShelfProduct) {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), DefaultValue.DEFAULT_ERROR_PARAM.value(), null);
        }

        String shopId = shopShelfProduct.getShopId();
        if (StringUtils.isEmpty(shopId)) {
            shopId = DefaultValue.DEFAULT_SHOPID.value();
        }
        String erpGoodsId = shopShelfProduct.getErpGoodsId();
        String barCode = shopShelfProduct.getBarCode();

        String searchParam = erpGoodsId;
        if (StringUtils.isEmpty(searchParam)) {
            searchParam = barCode;
        }

        if (StringUtils.isEmpty(shopId) || StringUtils.isEmpty(searchParam)) {
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), DefaultValue.DEFAULT_ERROR_PARAM.value(), null);
        }

        return shopShelfProductService.getLocationList(shopId, shopShelfProduct.getShelfNumber(), searchParam);
    }


}
