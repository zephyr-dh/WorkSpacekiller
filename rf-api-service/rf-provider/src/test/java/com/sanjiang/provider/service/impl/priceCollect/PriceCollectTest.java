package com.sanjiang.provider.service.impl.priceCollect;

import com.sanjiang.provider.service.BaseTest;
import com.sanjiang.provider.service.priceCollect.PriceCollectService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by byinbo on 2018/6/21.
 */
public class PriceCollectTest extends BaseTest {

    @Autowired
    private PriceCollectService priceCollectService;

    @Test
    public void testQuery(){
        System.out.println(priceCollectService.getSpmcAndGlbh("6926858908470","", ""));
    }
}
