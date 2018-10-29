package com.sanjiang.consumer.controller;

import com.sanjiang.annotation.ControllerLog;
import com.sanjiang.consumer.service.PreAllotConsumerService;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.core.ResultCode;
import com.sanjiang.provider.domain.preAllot.PreOrder;
import com.sanjiang.provider.domain.preAllot.PreSealBox;
import com.sanjiang.provider.domain.preAllot.PreTrunkBox;
import com.sanjiang.provider.model.preAllot.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前置仓订货
 *
 * @author wangpan
 * @date 2018/8/1 11:34
 */
@Api(tags = "PreAllotController", description = "前置仓店间调拨接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/preAllot", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PreAllotController {

    @Autowired
    private PreAllotConsumerService preAllotConsumerService;

    /**
     * 对方门店信息查询
     * @param storeId
     * @return
     */
    @ApiOperation(value = "查询对方门店编号和名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId", value = "门店id(必需)", required = true, dataType = "String",paramType = "query")
    })
    @GetMapping(value = "/search")
    @ControllerLog(description = "查询可以向其申请调货的对方门店ID+name")
    public ResponseEntity<Object> search(@RequestParam(name = "storeId") String storeId) {

        return ResponseEntity.ok(preAllotConsumerService.getOrderStore(storeId));
    }

    /**
     * 扫描商品后查询商品信息
     * @param storeId
     * @return
     */
    @ApiOperation(value = "扫描商品条码订货")
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId",value = "门店id（必需）",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "otherStoreId",value = "对方门店id（必需）",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "goodCode",value = "商品条码（必需）",required = true,dataType = "String",paramType = "query")
    })
    @GetMapping(value = "/findGood")
    @ControllerLog(description = "订货时扫描商品条码获得商品信息")
    public ResponseEntity<Object> getGoodMsg(@RequestParam(name = "storeId") String storeId,
                                             @RequestParam(name = "otherStoreId") String otherStoreId,
                                             @RequestParam(name = "goodCode") String goodCode){
        List<PreScanGoodModel> good = preAllotConsumerService.getPreGood(storeId,otherStoreId,goodCode);

        if (null == good) {
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.FAIL.code(), "查询订货商品失败", null));
        }
        return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "查询订货商品成功", good.get(0)));

    }

    /**
     * 查询历史订单
     * @param storeId
     * @return
     */
    @ApiOperation(value = "历史订货订单列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId",value = "门店id(必需)",required = true,dataType = "String",paramType = "query")})
    @GetMapping(value = "/historyOrder")
    @ControllerLog(description = "查询前置仓历史订货订单")
    public ResponseEntity<Object> getHistoryOrder(@RequestParam(name = "storeId") String storeId){

        List<PreOrdersModel> list = preAllotConsumerService.getPreOldOrders(storeId);
        if(null == list){
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.FAIL.code(),"查询历史订货订单失败",null ));
        }else{
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"查询历史订货订单成功",list));
        }
    }

    /**
     * 保存订货订单
     * @return
     */
    @ApiOperation(value = "保存订货订单")
    @PostMapping(value = "/insert")
    @ControllerLog(description = "保存前置仓订货订单")
    public ResponseEntity insertOrder(@RequestBody PreOrder preOrder){
        return ResponseEntity.ok(preAllotConsumerService.insertOrder(preOrder));
    }

    @ApiOperation(value = "分拣完成")
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId",value = "门店id(必需)",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "orderId",value = "订单id(必需)",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "userId",value = "操作员ID",required = true,dataType = "String",paramType = "query")
    })
    @GetMapping(value = "/finishPick")
    @ControllerLog(description = "订单分拣完成后修改订单状态")
    public ResponseEntity finishPick(@RequestParam(name = "storeId") String storeId,
                                     @RequestParam(name = "orderId") String orderId,
                                     @RequestParam(name = "userId") String userId){
        return ResponseEntity.ok(preAllotConsumerService.finishOrder(storeId, orderId, userId));
    }

    @ApiOperation(value = "调出订单列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId",value = "门店id(必需)",required = true,dataType = "String",paramType = "query")})
    @GetMapping(value = "/outOrder")
    @ControllerLog(description = "查询有效的调出订单")
    public ResponseEntity<Object> getOutOrder(@RequestParam(name = "storeId") String storeId){

        List<PreOrdersModel> list = preAllotConsumerService.getOutOrder(storeId);
        if(null == list){
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.FAIL.code(),"查询调出订单失败",null ));
        }else{
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"查询调出订单成功",list));
        }
    }

    @ApiOperation(value = "调出订单分拣商品列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId",value = "门店id(必需)",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "orderId",value = "订单id(必需)",required = true,dataType = "String",paramType = "query")})
    @GetMapping(value = "/outGood")
    @ControllerLog(description = "查询调出订单中的商品信息")
    public ResponseEntity<Object> getOutGood(@RequestParam(name = "storeId") String storeId,
                                             @RequestParam(name = "orderId") String orderId){
        List<PreOutGoodsModel> list = preAllotConsumerService.getOutGoods(storeId,orderId);

        if(null == list){
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.FAIL.code(),"查询调出商品失败",null));
        }else{
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"查询调出商品成功",list));
        }
    }


    @ApiOperation(value = "扫描商品条码调出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId", value = "门店id(必需)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "goodCode", value = "商品条码(必需)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单号(必需)", required = true, dataType = "String",paramType = "query")
    })
    @GetMapping(value = "/scanGood")
    @ControllerLog(description = "拣货时扫描商品条码检查商品库存")
    public ResponseEntity<Object> scanGoodCode(@RequestParam(name = "storeId") String storeId,
                                               @RequestParam(name = "goodCode") String goodCode,
                                               @RequestParam(name = "orderId") String orderId) {

        return ResponseEntity.ok(preAllotConsumerService.scanGoodCode(storeId,goodCode,orderId));
    }

    /**
     * 保存箱码信息
     * @param preSealBox
     * @return
     */
    @ApiOperation(value = "保存箱码")
    @PostMapping(value = "/insertBox")
    @ControllerLog(description = "先判断箱码是否重复，再保存箱码信息")
    public ResponseEntity insertBox(@RequestBody PreSealBox preSealBox){
        return ResponseEntity.ok(preAllotConsumerService.insertBoxGood(preSealBox));
    }

    @ApiOperation(value = "调出订单箱码信息列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId",value = "门店id(必需)",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "orderId",value = "订单id(必需)",required = true,dataType = "String",paramType = "query")})
    @GetMapping(value = "/outOrderInfo")
    @ControllerLog(description = "查询调出订单的详细信息")
    public ResponseEntity<Object> getOrderInfo(@RequestParam(name = "storeId") String storeId,
                                               @RequestParam(name = "orderId") String orderId){

        List<PreBoxModel> list = preAllotConsumerService.getOutOrderInfo(storeId,orderId);

        if(null == list){
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.FAIL.code(),"查询调出订单详情失败",null));
        }else{
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"查询调出订单详情成功",list));
        }
    }

    @ApiOperation(value = "调出箱码的商品信息列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId",value = "门店id(必需)",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "documentsId",value = "单据号(必需)",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "boxCode",value = "箱码号(必需)",required = true,dataType = "String",paramType = "query")})
    @GetMapping(value = "/outOrderBoxInfo")
    @ControllerLog(description = "查询调出订单中某箱码的详细信息")
    public ResponseEntity<Object> getOrderBoxInfo(@RequestParam(name = "storeId") String storeId,
                                                  @RequestParam(name = "documentsId") String documentsId,
                                               @RequestParam(name = "boxCode") String boxCode){

        List<PreOutGoodsModel> list = preAllotConsumerService.getgoodInBox(storeId,documentsId,boxCode);

        if(null == list){
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.FAIL.code(),"查询调出订单箱码详情失败",null));
        }else{
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"查询调出订单箱码详情成功",list));
        }
    }

    /**
     * 删除箱码
     * @return
     */
    @ApiOperation(value = "删除箱码")
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId",value = "门店id(必需)",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "documentsId",value = "单据号(必需)",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "boxCode",value = "箱码号(必需)",required = true,dataType = "String",paramType = "query")})
    @GetMapping(value = "/delBox")
    @ControllerLog(description = "删除箱码")
    public ResponseEntity delBox(@RequestParam(name = "storeId") String storeId,
                                 @RequestParam(name = "documentsId") String documentsId,
                                 @RequestParam(name = "boxCode") String boxCode){
        return ResponseEntity.ok(preAllotConsumerService.delBox(storeId,documentsId,boxCode));
    }

    /**
     * 删除箱中商品
     * @return
     */
    @ApiOperation(value = "删除箱中商品")
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId",value = "门店id(必需)",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "documentsId",value = "单据号(必需)",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "boxCode",value = "箱码号(必需)",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "goodId",value = "商品管理码（必需）",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "orderId",value = "订单号（必需）",required = true,dataType = "String",paramType = "query")})
    @GetMapping(value = "/delGoodInBox")
    @ControllerLog(description = "删除箱中商品")
    public ResponseEntity delGoodInBox(@RequestParam(name = "storeId") String storeId,
                                 @RequestParam(name = "documentsId") String documentsId,
                                 @RequestParam(name = "boxCode") String boxCode,
                                 @RequestParam(name = "goodId") String goodId,
                                  @RequestParam(name = "orderId") String orderId){
        return ResponseEntity.ok(preAllotConsumerService.delGoodInBox(storeId,documentsId,boxCode,goodId,orderId));
    }

    @ApiOperation(value = "装车订单列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId",value = "门店id(必需)",required = true,dataType = "String",paramType = "query")})
    @GetMapping(value = "/trunkInfo")
    @ControllerLog(description = "查询调出订单的详细信息")
    public ResponseEntity getTrunkInfo(@RequestParam(name = "storeId") String storeId){
        List<PreBoxModel> list = preAllotConsumerService.getTrunkInfo(storeId);
        if(null == list){
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.FAIL.code(),"装车列表查询失败",null));
        }else{
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"装车列表查询成功",list));
        }
    }

    @ApiOperation(value = "装车")
    @PostMapping(value = "/trunkLoad")
    @ControllerLog(description = "扫描箱码后点击装车")
    public ResponseEntity trunkLoad(@RequestBody PreTrunkBox preTrunkBox){
        return ResponseEntity.ok(preAllotConsumerService.trunkLoad(preTrunkBox));
    }

    @ApiOperation(value = "入库订单列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId",value = "门店id(必需)",required = true,dataType = "String",paramType = "query")})
    @GetMapping(value = "/storageInfo")
    @ControllerLog(description = "查询入库订单的详细信息")
    public ResponseEntity getStorageOrderInfo(@RequestParam(name = "storeId") String storeId){
        List<PreReceiveOrderModel> list = preAllotConsumerService.getStorageOrderInfo(storeId);
        if(null == list){
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.FAIL.code(),"查询入库订单失败",null));
        }else{
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"查询入库订单成功",list));
        }
    }

    @ApiOperation(value = "入库订单箱码详情")
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId",value = "门店id(必需)",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "otherStoreId",value = "对方门店ID(必需)",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "documentsId",value = "单据号(必需)",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "boxCode",value = "箱码(必需)",required = true,dataType = "String",paramType = "query")})
    @GetMapping(value = "/storageBoxInfo")
    @ControllerLog(description = "入库订单箱码详情")
    public ResponseEntity getStorageBoxInfo(@RequestParam(name = "storeId") String storeId,
                                            @RequestParam(name = "otherStoreId") String otherStoreId,
                                            @RequestParam(name = "boxCode") String boxCode,
                                            @RequestParam(name = "documentsId") String documentsId){
        List<PreOutGoodsModel> list = preAllotConsumerService.getStorageBoxInfo(storeId, otherStoreId, documentsId,boxCode);
        if(null == list){
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.FAIL.code(),"查询入库订单箱码信息失败",null));
        }else{
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"查询入库订单箱码信息成功",list));
        }
    }

    @ApiOperation(value = "扫描箱码入库")
    @ApiImplicitParams({@ApiImplicitParam(name = "uuid",value = "uuid(必需)",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "storeId",value = "门店id(必需)",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "boxCode",value = "箱码(必需)",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "userId",value = "操作员ID",required = true,dataType = "String",paramType = "query")})
    @GetMapping(value = "/boxInstorage")
    @ControllerLog(description = "扫描箱码入库")
    public ResponseEntity boxInStorage(@RequestParam(name = "uuid") String uuid,
                                       @RequestParam(name = "storeId") String storeId,
                                       @RequestParam(name = "boxCode") String boxCode,
                                       @RequestParam(name = "userId") String userId){
        return ResponseEntity.ok(preAllotConsumerService.scanBoxInStorage(uuid, storeId, boxCode,userId));
    }
}
