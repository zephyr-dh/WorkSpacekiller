package com.sanjiang.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.provider.model.NoOrderCollectGoods;
import com.sanjiang.provider.service.noOrderCollect.NoOrderCollectService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by byinbo on 2018/6/25.
 */
@Service
public class NoOrderCollectConsumerService {

    @Reference(
            application = "${dubbo.application.id}",
            registry = "${dubbo.registry.id}",
//            url = "dubbo://193.0.1.229:20880",
            version = "1.0.0",
            timeout = 50000
    )
    private NoOrderCollectService noOrderCollectService;

    public ResponseMessage getSuppliers(String scbh, String bmbh){
        return noOrderCollectService.getSuppliers(scbh, bmbh);
    }

    public ResponseMessage getProductBySpbh(String scbh,String spbh){
        return noOrderCollectService.getProductBySpbh(scbh,spbh);
    }

    public ResponseMessage save(List<NoOrderCollectGoods> noOrderCollectGoodsList){
        return noOrderCollectService.save(noOrderCollectGoodsList);
    }

}
