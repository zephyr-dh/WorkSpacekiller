package com.sanjiang.provider.mapper;

import com.sanjiang.provider.domain.ShopDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShopDomainMapper {

    ShopDomain findByShopId(@Param("BMBH") String shopId);
}
