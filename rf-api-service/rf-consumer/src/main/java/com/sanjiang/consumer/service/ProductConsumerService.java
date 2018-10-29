package com.sanjiang.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sanjiang.provider.domain.ProductDomain;
import com.sanjiang.provider.model.ProductSearch;
import com.sanjiang.provider.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kimiyu
 * @date 2018/4/17 13:58
 */
@Service
public class ProductConsumerService {

    @Reference(
            application = "${dubbo.application.id}",
            registry = "${dubbo.registry.id}",
//            url = "dubbo://193.0.1.116:20880",
            group = "product",
            version = "1.0.1",
            timeout = 3000
    )
    private ProductService productService;


    public List<ProductDomain> getProducts(String shopId, String spbm, String deviceId, String workerId) {

        ProductSearch productSearch = new ProductSearch();
        productSearch.setSpbm(spbm);
        productSearch.setDeviceId(deviceId);
        productSearch.setWorkerId(workerId);
        productSearch.setShopId(shopId);
        return productService.getBySPBMNew(productSearch);
    }
}
