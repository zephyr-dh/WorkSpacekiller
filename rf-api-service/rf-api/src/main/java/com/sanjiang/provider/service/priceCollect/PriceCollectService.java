package com.sanjiang.provider.service.priceCollect;

import com.sanjiang.core.ResponseMessage;
import com.sanjiang.provider.model.PriceCollectModel;

/**
 * Created by byinbo on 2018/6/15.
 */
public interface PriceCollectService {

    ResponseMessage<Object> getSpmcAndGlbh(String spbh,String scname, String czy);

    ResponseMessage saveData(PriceCollectModel priceCollectModel);

    ResponseMessage count(String czy);

}
