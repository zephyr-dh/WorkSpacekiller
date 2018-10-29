package com.sanjiang.provider.service.preAllot;

import com.sanjiang.core.ResponseMessage;
import com.sanjiang.provider.domain.preAllot.PreSealBox;
import com.sanjiang.provider.model.preAllot.PreOrdersModel;
import com.sanjiang.provider.model.preAllot.PreOutGoodsModel;
import com.sanjiang.provider.model.preAllot.PreBoxModel;

import java.util.List;

/**
 * 前置仓拣货
 * created by wangpan on 2018/7/31
 */
public interface PreAllotPickService {

    /**
     * 调出所有有效订单
     * @param stroeId
     * @return
     */
    List<PreOrdersModel> getPreOrder(String stroeId);

    /**
     * 查询某未分拣订单下的商品详情
     * @param storeId
     * @param orderId
     * @return
     */
    List<PreOutGoodsModel> getPreGoodMsg(String storeId, String orderId);

    /**
     * 扫描商品条码准备装箱
     * @param storeId
     * @param goodCode
     * @return
     */
    ResponseMessage scanGoodCode(String storeId,String goodCode,String orderId);

    /**
     * 将装箱后的箱子插入数据库中
     * @param preSealBox
     * @return
     */
    ResponseMessage insertBoxGood(PreSealBox preSealBox);


    /**
     * 调出订单详情---订单下的箱码信息
     * @param storeId
     * @param orderId
     * @return
     */
    List<PreBoxModel> getOutOrderDetail(String storeId, String orderId);

    /**
     * 查看某个箱码的详情
     * @param storeId
     * @param documentsId
     * @param boxCode
     * @return
     */
    List<PreOutGoodsModel> getBoxMsg(String storeId,String documentsId,String boxCode);


    /**
     * 删除箱码
     * @param storeId
     * @param documentsId
     * @param boxCode
     * @return
     */
    ResponseMessage delBox(String storeId,String documentsId,String boxCode);

    /**
     * 删除箱码中的某个商品
     * @param storeId
     * @param documentsId
     * @param boxCode
     * @param goodId
     * @param orderId
     * @return
     */
    ResponseMessage delGoodInBox(String storeId, String documentsId, String boxCode, String goodId,String orderId);

    /**
     * 订单分拣完成
     * @param storeId
     * @param orderId
     * @param userId
     * @return
     */
    ResponseMessage finishPick(String storeId,String orderId,String userId);
}
