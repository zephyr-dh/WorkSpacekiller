package com.sanjiang.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.provider.model.CollectGoodsModel;
import com.sanjiang.provider.model.PreCheckModel;
import com.sanjiang.provider.service.collectGoods.PreCheckListService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by byinbo on 2018/5/23.
 */
@Service
public class PreCheckConsumerService {
    @Reference(
            application = "${dubbo.application.id}",
            registry = "${dubbo.registry.id}",
//            url = "dubbo://193.0.1.229:20880",
            version = "1.0.0",
            timeout = 50000
    )
    private PreCheckListService preCheckListService;

    public ResponseMessage getReportPreCheck(String scbh){
        return preCheckListService.getReportPreCheckList(scbh);
    }

    public ResponseMessage queryPreCheck(String scbh, String bmbh, String cch){
        return preCheckListService.queryPreCheckList(scbh, bmbh, cch);
    }

    public ResponseMessage createPreCheck(String scbh, String uuid){
        return preCheckListService.createPreCheckList(scbh, uuid);
    }

    public ResponseMessage inspectGoodsList(String scbh, String djdh){
        return preCheckListService.inspectGoodsList(scbh, djdh);
    }

    public ResponseMessage verifyPreCheckList(String scbh, String djdh){
        return preCheckListService.verifyPreCheckList(scbh, djdh);
    }

    public ResponseMessage uploadData(List<CollectGoodsModel> collectGoodsModels){
        return preCheckListService.uploadData(collectGoodsModels);
    }

    public ResponseMessage insertPreCheckData(List<PreCheckModel> preCheckModel){
        return preCheckListService.insertPreCheckData(preCheckModel);
    }

}
