package com.sanjiang.provider.service.impl.exhibitionwarehouse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanjiang.provider.domain.exhibitionwarehouse.ShopShelfProduct;
import com.sanjiang.provider.service.BaseTest;
import com.sanjiang.provider.service.exhibitionwarehouse.ShopShelfProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Slf4j
public class ShopShelfProductServiceImplTest extends BaseTest {

    @Autowired
    private ShopShelfProductService shopShelfProductService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void save() {

        ShopShelfProduct shopShelfProduct = new ShopShelfProduct();
        shopShelfProduct.setShopId("00003");
        shopShelfProduct.setShelfNumber("233");
        shopShelfProduct.setErpGoodsId("1150002");
        shopShelfProduct.setLayerNumber(0);
//        shopShelfProduct.setLocation();
        shopShelfProduct.setBarCode("6921168509256");

        System.out.println(shopShelfProductService.save(shopShelfProduct));

    }

//    @Test
//    public void list() {
//
//        try {
//            log.info("result: {}", objectMapper.writeValueAsString(shopShelfProductService.list("00003", "233", 0)));
//        } catch (IOException ex) {
//
//        }
//    }

    @Test
    public void deleteByLayerNumber() {


        System.out.println(shopShelfProductService.deleteByLayerNumber("00023", "1234567890", 0));
    }

    @Test
    public void deleteById() {

        System.out.println(shopShelfProductService.deleteById("00023", "1234567890", 22l));
    }

    @Test
    public void getLocationList() {

        System.out.println(shopShelfProductService.getLocationList("00003", "", "6922711000695"));
    }
}