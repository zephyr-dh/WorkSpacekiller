package com.sanjiang.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.provider.domain.preAllot.PreOrder;
import com.sanjiang.provider.domain.preAllot.PreSealBox;
import com.sanjiang.provider.domain.preAllot.PreTrunkBox;
import com.sanjiang.provider.model.preAllot.*;
import com.sanjiang.provider.service.preAllot.PreAllotService;
import com.sanjiang.provider.service.preAllot.PreAllotPickService;
import com.sanjiang.provider.service.preAllot.PreAllotSendAndReviceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangpan on 2018/8/1.
 */
@Service
public class PreAllotConsumerService {

    @Reference(
            application = "${dubbo.application.id}",
            registry = "${dubbo.registry.id}",
            version = "1.0.0",
            timeout = 3000
    )
    private PreAllotService preAllotService;             //前置仓订货服务

    @Reference(
            application = "${dubbo.application.id}",
            registry = "${dubbo.registry.id}",
            version = "1.0.0",
            timeout = 3000
    )
    private PreAllotPickService preAllotPickService;     //前置仓拣货服务

    @Reference(
            application = "${dubbo.application.id}",
            registry = "${dubbo.registry.id}",
            version = "1.0.0",
            timeout = 3000
    )
    private PreAllotSendAndReviceService preAllotSendAndReceiveService;  //前置仓出车和入库服务

    public ResponseMessage getOrderStore(String storeId){
        return preAllotService.getOrderStore(storeId);
    }

    public List<PreScanGoodModel> getPreGood(String storeId, String otherStoreId, String goodCode){
        return preAllotService.getPreGood(storeId,otherStoreId,goodCode);
    }

    public List<PreOrdersModel> getPreOldOrders(String storeId){
        return preAllotService.getPreOldOrders(storeId);
    }

    public ResponseMessage insertOrder(PreOrder preOrder){

        return preAllotService.insertOrder(preOrder.getStoreId(),preOrder.getOtherStoreId(),preOrder.getUserId(),preOrder.getGoodList());
    }

    public ResponseMessage finishOrder(String storeId,String orderId,String userId){
        return preAllotPickService.finishPick(storeId, orderId, userId);
    }

    public List<PreOrdersModel> getOutOrder(String storeId){
        return preAllotPickService.getPreOrder(storeId);
    }

    public List<PreOutGoodsModel> getOutGoods(String storeId,String orderId){
        return preAllotPickService.getPreGoodMsg(storeId,orderId);
    }

    public ResponseMessage scanGoodCode(String storeId,String goodCode,String orderId){
        return preAllotPickService.scanGoodCode(storeId,goodCode,orderId);
    }

    public ResponseMessage insertBoxGood(PreSealBox preSealBox){
        return preAllotPickService.insertBoxGood(preSealBox);
    }

    public List<PreBoxModel> getOutOrderInfo(String storeId, String orderId){
        return preAllotPickService.getOutOrderDetail(storeId,orderId);
    }

    public List<PreOutGoodsModel> getgoodInBox(String storeId,String documentsId,String boxCode){
        return preAllotPickService.getBoxMsg(storeId, documentsId, boxCode);
    }

    public ResponseMessage delBox(String storeId,String documentId,String boxCode){
        return preAllotPickService.delBox(storeId, documentId, boxCode);
    }

    public  ResponseMessage delGoodInBox(String storeId,String documentId,String boxCode,String goodId,String orderId){
        return preAllotPickService.delGoodInBox(storeId, documentId, boxCode, goodId, orderId);
    }

    public List<PreBoxModel> getTrunkInfo(String storeId){
        return preAllotSendAndReceiveService.getTrunkInfo(storeId);
    }

    public ResponseMessage trunkLoad(PreTrunkBox preTrunkBox){
        return preAllotSendAndReceiveService.trunkLoad(preTrunkBox);
    }

    public List<PreReceiveOrderModel> getStorageOrderInfo(String storeId){
        return preAllotSendAndReceiveService.getReceviceOrderInfo(storeId);
    }

    public List<PreOutGoodsModel> getStorageBoxInfo(String storeId, String otherStoreId, String documentsId, String boxCode){
        return preAllotSendAndReceiveService.getReceiveBoxGoodInfo(storeId, otherStoreId, documentsId, boxCode);
    }

    public ResponseMessage scanBoxInStorage(String uuid,String storeId,String boxCode,String userId) {
        return preAllotSendAndReceiveService.scanBoxInStorage(uuid, storeId,  boxCode,userId);
    }

}
