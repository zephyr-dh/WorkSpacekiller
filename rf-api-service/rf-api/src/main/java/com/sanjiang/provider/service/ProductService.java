package com.sanjiang.provider.service;

import com.sanjiang.provider.domain.ProductDomain;
import com.sanjiang.provider.model.ProductSearch;

import java.util.List;

/**
 * 商品服务
 */
public interface ProductService {

    /**
     * 根据门店号和sp编码等查询对应商品信息
     *
     * @param productSearch 商品搜索模型
     * @return
     */
    List<ProductDomain> getBySPBM(ProductSearch productSearch);

    /**
     * 根据门店号和sp编码等查询对应商品信息[新]
     *
     * @param productSearch 商品搜索模型
     * @return
     */
    List<ProductDomain> getBySPBMNew(ProductSearch productSearch);
}
