package com.sanjiang.provider.service;

import com.sanjiang.provider.service.goodsoutoftime.PreWarnShelfLifeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;


public class ShopServiceTest extends BaseTest {

    @Autowired
    private ShopService shopService;

    @Autowired
    private PreWarnShelfLifeService preWarnShelfLifeService;

    @Test
    public void getByShopId() {

        System.out.println(shopService.getByShopId("00143"));
    }

    @Test
    public void getPreWarn(){
        System.out.println(preWarnShelfLifeService.goodsOutOfTimeCx("hzcx","00003", null, null, null));
    }

    @Test
    public void createPreWarn(){
        System.out.println(preWarnShelfLifeService.goodsOutOfTimeCreat("00003", new String[]{"50"}));
    }

    @Test
    public void checkGoods(){
        System.out.println(preWarnShelfLifeService.goodsOutOfTimeCz("delete","00003", "5110712", "2018-05-11",null,"处理人员",null ));
    }
}