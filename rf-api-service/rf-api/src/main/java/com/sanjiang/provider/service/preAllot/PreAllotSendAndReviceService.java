package com.sanjiang.provider.service.preAllot;


import com.sanjiang.core.ResponseMessage;
import com.sanjiang.provider.domain.preAllot.PreTrunkBox;
import com.sanjiang.provider.model.preAllot.PreBoxModel;
import com.sanjiang.provider.model.preAllot.PreOutGoodsModel;
import com.sanjiang.provider.model.preAllot.PreReceiveOrderModel;

import java.util.List;

/**
 * 前置仓装车入库
 * created by wangpan on 2018/7/31
 */
public interface PreAllotSendAndReviceService {

    /**
     * 查询需装车的箱码信息
     * @param storeId
     * @return
     */
    List<PreBoxModel> getTrunkInfo(String storeId);

    /**
     * 装车
     * @param preTrunkBox
     * @return
     */
    ResponseMessage trunkLoad(PreTrunkBox preTrunkBox);

    /**
     * 调入订单
     * @param storeId
     * @return
     */
    List<PreReceiveOrderModel> getReceviceOrderInfo(String storeId);

    /**
     * 查看调入订单某箱码内的商品信息
     * @return
     */
    List<PreOutGoodsModel> getReceiveBoxGoodInfo(String storeId,String otherStoreId,String documentsId,String boxCode);

    /**
     * 扫描箱码页面会返回该箱内信息并且直接默认入库
     * @param storeId
     * @param boxCode
     * @param userId
     * @return
     */
    ResponseMessage scanBoxInStorage(String uuid,String storeId, String boxCode,String userId);


}
