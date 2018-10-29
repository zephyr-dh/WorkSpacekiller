package com.sanjiang.provider.service.impl.collectGoods;

import com.sanjiang.provider.service.BaseTest;
import com.sanjiang.provider.service.collectGoods.PreCheckListService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by byinbo on 2018/5/21.
 */
public class PreCheckListTest extends BaseTest {

    @Autowired
    private PreCheckListService preCheckListService;

    @Test
    public void preCheckGoods(){
        System.out.println(preCheckListService.getReportPreCheckList("00001"));
    }

    @Test
    public void queryPreCheckGoods(){
        System.out.println(preCheckListService.queryPreCheckList("00001","402529", "2230260"));
    }

    @Test
    public void createPreCheckList(){
        System.out.println(preCheckListService.createPreCheckList("00001", "12345654321"));
    }

    @Test
    public void inspectGoodsList(){
        System.out.println(preCheckListService.inspectGoodsList("00001", "403598"));
    }
}
