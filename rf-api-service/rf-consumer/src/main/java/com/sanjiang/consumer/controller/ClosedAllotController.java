package com.sanjiang.consumer.controller;

import com.sanjiang.annotation.ControllerLog;
import com.sanjiang.consumer.service.ClosedAllotConsumerService;
import com.sanjiang.provider.model.ClosedAllot.ClosedAllotModel;
import com.sanjiang.provider.model.ClosedAllot.DbckSearch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by byinbo on 2018/7/8.
 */
@Api(tags = "ClosedAllotController", description = "闭店调拨接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/closedAllot", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ClosedAllotController {

    @Autowired
    private ClosedAllotConsumerService closedAllotConsumerService;

    @ApiOperation(value = "获取对方门店列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scbh", value = "门店id", required = false, dataType = "String",paramType = "query")
    })
    @GetMapping("/getShops")
    @ControllerLog(description = "获取对方门店列表")
    public ResponseEntity queryShop(@RequestParam(name = "scbh", required = false) String scbh) {
        return ResponseEntity.ok(closedAllotConsumerService.queryShop(scbh));
    }

    @ApiOperation(value = "根据商品条码查询非休眠、删除、淘汰商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scbh", value = "门店id", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "spbh", value = "商品条码", required = true, dataType = "String",paramType = "query")

    })
    @GetMapping("/getGoodsBySpbh")
    @ControllerLog(description = "根据商品条码查询非休眠、删除、淘汰商品")
    public ResponseEntity queryGoodsBySpbhExcept(@RequestParam("scbh") String scbh, @RequestParam("spbh") String spbh) {
        return ResponseEntity.ok(closedAllotConsumerService.queryGoodsBySpbhExcept(scbh, spbh));
    }

    @ApiOperation(value = "根据区域查询已录入商品")
    @PostMapping("/getGoodsByArea")
    @ControllerLog(description = "根据区域查询已录入商品")
    public ResponseEntity queryGoodsByArea(@RequestBody DbckSearch dbckSearch) {
        return ResponseEntity.ok(closedAllotConsumerService.queryGoodsByArea(dbckSearch));
    }

    @ApiOperation(value = "保存调拨出库数据")
    @PostMapping("/saveDbck")
    @ControllerLog(description = "保存调拨出库数据")
    public ResponseEntity saveDbck(@RequestBody List<ClosedAllotModel> closedAllotModel) {
        return ResponseEntity.ok(closedAllotConsumerService.saveDbck(closedAllotModel));
    }

    @ApiOperation(value = "根据序号删除调拨出库数据")
    @PostMapping("/deleteDbckByXh")
    @ControllerLog(description = "根据序号删除调拨出库数据")
    public ResponseEntity deleteDbckByXh(@RequestBody DbckSearch dbckSearch) {
        return ResponseEntity.ok(closedAllotConsumerService.deleteDbckByXh(dbckSearch));
    }

    @ApiOperation(value = "查询区域明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scbh", value = "门店id", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "bmbh", value = "对方门店id", required = true, dataType = "String",paramType = "query")

    })
    @GetMapping("/getAreaDetail")
    @ControllerLog(description = "查询区域明细")
    public ResponseEntity listDbcks(String scbh, String bmbh) {
        return ResponseEntity.ok(closedAllotConsumerService.listDbcks(scbh, bmbh));
    }

    @ApiOperation(value = "根据区域删除调拨出库数据")
    @PostMapping("/deleteDbckByBh")
    @ControllerLog(description = "根据区域删除调拨出库数据")
    public ResponseEntity deleteDbckByBh(@RequestBody DbckSearch dbckSearch) {
        return ResponseEntity.ok(closedAllotConsumerService.deleteDbckByBh(dbckSearch));
    }

    @ApiOperation(value = "查询区域商品明细")
    @PostMapping("/getAreaGoodsDetail")
    @ControllerLog(description = "查询区域商品明细")
    public ResponseEntity listBhGoods(@RequestBody DbckSearch dbckSearch) {
        return ResponseEntity.ok(closedAllotConsumerService.listBhGoods(dbckSearch));
    }

    @ApiOperation(value = "完成调拨")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scbh", value = "门店id", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "bmbh", value = "对方门店id", required = true, dataType = "String",paramType = "query")

    })
    @GetMapping("/finishedAllot")
    @ControllerLog(description = "完成调拨")
    public ResponseEntity finishedAllot(String scbh, String bmbh) {
        return ResponseEntity.ok(closedAllotConsumerService.finishedAllot(scbh, bmbh));
    }
}
