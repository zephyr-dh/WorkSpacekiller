package com.sanjiang.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.provider.model.PriceCollectModel;
import com.sanjiang.provider.service.priceCollect.PriceCollectService;
import org.springframework.stereotype.Service;

/**
 * Created by byinbo on 2018/6/21.
 */
@Service
public class PriceCollectConsumerService {

    @Reference(
            application = "${dubbo.application.id}",
            registry = "${dubbo.registry.id}",
//            url = "dubbo://193.0.1.229:20880",
            version = "1.0.0",
            timeout = 50000
    )
    private PriceCollectService priceCollectService;


    public ResponseMessage query(String spbh, String scname, String czy){
        return priceCollectService.getSpmcAndGlbh(spbh,scname,czy);
    }

    public ResponseMessage save(PriceCollectModel priceCollectModel){
        return priceCollectService.saveData(priceCollectModel);
    }

    public ResponseMessage count(String czy){
        return priceCollectService.count(czy);
    }


}
