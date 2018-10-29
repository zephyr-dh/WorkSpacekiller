package com.sanjiang.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.provider.model.ClosedAllot.ClosedAllotModel;
import com.sanjiang.provider.model.ClosedAllot.DbckSearch;
import com.sanjiang.provider.service.closedAllot.ClosedAllotService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by byinbo on 2018/7/8.
 */
@Service
public class ClosedAllotConsumerService {

    @Reference(
            application = "${dubbo.application.id}",
            registry = "${dubbo.registry.id}",
//            url = "dubbo://193.0.1.229:20880",
            version = "1.0.0",
            timeout = 50000
    )
    private ClosedAllotService closedAllotService;


    public ResponseMessage queryShop(String scbh) {
        return closedAllotService.queryShop(scbh);
    }

    public ResponseMessage queryGoodsBySpbhExcept(String scbh, String spbh) {
        return closedAllotService.queryGoodsBySpbhExcept(scbh, spbh);
    }

    public ResponseMessage queryGoodsByArea(DbckSearch dbckSearch) {
        return closedAllotService.queryGoodsByArea(dbckSearch);
    }

    public ResponseMessage saveDbck(List<ClosedAllotModel> closedAllotModel) {
        return closedAllotService.saveDbck(closedAllotModel);
    }

    public ResponseMessage deleteDbckByXh(DbckSearch dbckSearch) {
        return closedAllotService.deleteDbckByXh(dbckSearch);
    }

    public ResponseMessage listDbcks(String scbh, String bmbh) {
        return closedAllotService.listDbcks(scbh, bmbh);
    }

    public ResponseMessage deleteDbckByBh(DbckSearch dbckSearch) {
        return closedAllotService.deleteDbckByBh(dbckSearch);
    }

    public ResponseMessage listBhGoods(DbckSearch dbckSearch) {
        return closedAllotService.listBhGoods(dbckSearch);
    }

    public ResponseMessage finishedAllot(String scbh, String bmbh) {
        return closedAllotService.finishedAllot(scbh, bmbh);
    }
}
