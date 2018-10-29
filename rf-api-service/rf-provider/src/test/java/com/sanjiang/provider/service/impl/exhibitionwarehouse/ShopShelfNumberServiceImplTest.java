package com.sanjiang.provider.service.impl.exhibitionwarehouse;

import com.sanjiang.provider.domain.exhibitionwarehouse.ShopShelfNumber;
import com.sanjiang.provider.service.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShopShelfNumberServiceImplTest extends BaseTest {

    @Autowired
    private ShopShelfNumberServiceImpl shopShelfNumberService;

    @Test
    public void save() {

        ShopShelfNumber shopShelfNumber = new ShopShelfNumber();
        shopShelfNumber.setShopId("00023");
        shopShelfNumber.setShelfArea("Y");
        shopShelfNumber.setShelfDisplayType("CB");
        shopShelfNumber.setShelfNumber("1234567890");

        System.out.println(shopShelfNumberService.save(shopShelfNumber));

    }

    @Test
    public void findByShopIdAndShelfNumber() {

        System.out.println(shopShelfNumberService.findByShopIdAndShelfNumber("00003", "7006"));
    }

    @Test
    public void getShelfConstants() {

        System.out.println(shopShelfNumberService.getShelfConstants("00023"));
    }

    @Test
    public void getShelfs() {

        System.out.println(shopShelfNumberService.getShelfs("00003"));
    }
}