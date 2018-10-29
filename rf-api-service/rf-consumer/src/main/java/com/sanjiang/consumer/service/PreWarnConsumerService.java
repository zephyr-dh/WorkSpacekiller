package com.sanjiang.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.provider.service.goodsoutoftime.PreWarnShelfLifeService;
import org.springframework.stereotype.Service;

/**
 * Created by byinbo on 2018/5/14.
 */
@Service
public class PreWarnConsumerService {

    @Reference(
            application = "${dubbo.application.id}",
            registry = "${dubbo.registry.id}",
//            url = "dubbo://193.0.1.229:20880",
            version = "1.0.0",
            timeout = 3000
    )
    private PreWarnShelfLifeService preWarnShelfLifeService;

    public ResponseMessage getPreWarn(String report,
                                      String shopId,
                                      String jcrq,
                                      String fzm,
                                      String spbh){
        return preWarnShelfLifeService.goodsOutOfTimeCx(report, shopId, jcrq, fzm, spbh);
    }

    public ResponseMessage createPreWarn(String shopId, String[] fzm){
        return preWarnShelfLifeService.goodsOutOfTimeCreat(shopId, fzm);
    }

    public ResponseMessage checkGoods(String czlx, String shopId, String glbh, String scrq, String hjbh, String clry, String jcrq){
        return preWarnShelfLifeService.goodsOutOfTimeCz(czlx, shopId, glbh, scrq, hjbh, clry, jcrq);
    }

}
