package com.sanjiang.provider.service.preAllot;


import com.sanjiang.core.ResponseMessage;
import com.sanjiang.provider.domain.preAllot.PreOrderGood;
import com.sanjiang.provider.model.preAllot.PreScanGoodModel;
import com.sanjiang.provider.model.preAllot.PreOrdersModel;

import java.util.List;

/**
 * 前置仓店间调拨订货、拣货、出车、入库
 * created by wangpan on 2018/7/31
 */
public interface PreAllotService {


    /**
     * 获得对方门店编号及名称
     * @param stroeId
     * @return
     */
    ResponseMessage getOrderStore(String stroeId);

    /**
     * 输入商品条码查询本店商品信息
     * @param storeId
     * @param goodId
     * @return
     */
    List<PreScanGoodModel> getPreGood(String storeId, String otherStoreId, String goodId);


    /**
     * 查询历史订单
     * @param storeId
     * @return
     *
     */
    List<PreOrdersModel> getPreOldOrders(String storeId);

    /**
     * 插入订单
     * @param storeId  本地商场编号
     * @param otherStoreId  对方商场编号
     * @param goodList  商品对象
     * @param userId    登陆移动设备的人员ID
     * @return
     */
    ResponseMessage insertOrder(String storeId, String otherStoreId, String userId, List<PreOrderGood> goodList);

}
