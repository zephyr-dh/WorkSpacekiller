package com.sanjiang.provider.service;

import com.sanjiang.provider.domain.ShopDomain;
import com.sanjiang.provider.mapper.ShopDomainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 门店服务
 *
 * @author kimiyu
 * @date 2018/4/16 11:41
 */
@Service
public class ShopService {

    @Autowired
    private ShopDomainMapper shopDomainMapper;


    public ShopDomain getByShopId(String shopId) {
        return shopDomainMapper.findByShopId(shopId);
    }
}
