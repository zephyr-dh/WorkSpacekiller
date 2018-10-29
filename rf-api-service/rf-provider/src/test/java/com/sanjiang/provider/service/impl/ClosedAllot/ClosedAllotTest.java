package com.sanjiang.provider.service.impl.ClosedAllot;

import com.sanjiang.provider.service.BaseTest;
import com.sanjiang.provider.service.closedAllot.ClosedAllotService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by byinbo on 2018/7/8.
 */
public class ClosedAllotTest extends BaseTest{

    @Autowired
    private ClosedAllotService closedAllotService;

    @Test
    public void queryShop(){
        System.out.println(closedAllotService.queryShop("00016"));
    }

    @Test
    public void queryGoodsBySpbhExcept(){
        System.out.println(closedAllotService.queryGoodsBySpbhExcept("00001","6901209212215"));
    }

}
