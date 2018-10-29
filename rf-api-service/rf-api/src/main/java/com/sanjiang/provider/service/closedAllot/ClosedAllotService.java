package com.sanjiang.provider.service.closedAllot;

import com.sanjiang.core.ResponseMessage;
import com.sanjiang.provider.model.ClosedAllot.ClosedAllotModel;
import com.sanjiang.provider.model.ClosedAllot.DbckSearch;

import java.util.List;

/**
 * 闭店调拨
 * Created by byinbo on 2018/7/6.
 */
public interface ClosedAllotService {

    ResponseMessage queryShop(String scbh);

    ResponseMessage queryGoodsBySpbhExcept(String scbh, String spbh);

    ResponseMessage queryGoodsByArea(DbckSearch dbckSearch);

    ResponseMessage saveDbck(List<ClosedAllotModel> closedAllotModel);

    ResponseMessage deleteDbckByXh(DbckSearch dbckSearch);

    ResponseMessage listDbcks(String scbh, String bmbh);

    ResponseMessage deleteDbckByBh(DbckSearch dbckSearch);

    ResponseMessage listBhGoods(DbckSearch dbckSearch);

    ResponseMessage finishedAllot(String scbh, String bmbh);


}
