package com.sanjiang.provider.service.noOrderCollect;

import com.sanjiang.core.ResponseMessage;
import com.sanjiang.provider.model.NoOrderCollectGoods;

import java.util.List;

/**
 * Created by byinbo on 2018/6/25.
 */
public interface NoOrderCollectService {

    ResponseMessage getSuppliers(String scbh, String bmbh);

    ResponseMessage getProductBySpbh(String scbh,String spbh);

    ResponseMessage save(List<NoOrderCollectGoods> noOrderCollectGoodsList);
}
