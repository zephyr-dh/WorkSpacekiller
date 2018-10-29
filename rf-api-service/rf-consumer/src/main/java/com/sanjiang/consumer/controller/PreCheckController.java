package com.sanjiang.consumer.controller;

import com.sanjiang.annotation.ControllerLog;
import com.sanjiang.consumer.service.PreCheckConsumerService;
import com.sanjiang.provider.domain.exhibitionwarehouse.ShopShelfProduct;
import com.sanjiang.provider.model.CollectGoodsModel;
import com.sanjiang.provider.model.PreCheckModel;
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
 * Created by byinbo on 2018/5/23.
 */
@Api(tags = "PreCheckController", description = "预检单收货接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/preCheck", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PreCheckController {

    @Autowired
    private PreCheckConsumerService preCheckConsumerService;

    @ApiOperation(value = "显示15天内未生成预检单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "门店id(必需)", required = true, dataType = "String",paramType = "query")
    })
    @GetMapping("/report")
    @ControllerLog(description = "显示15天内未生成预检单")
    public ResponseEntity<Object> getReportPreCheck(@RequestParam(name = "shopId") String shopId){
        return ResponseEntity.ok(preCheckConsumerService.getReportPreCheck(shopId));
    }

    @ApiOperation(value = "查询预检单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "门店id", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "bmbh", value = "部门编号", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "cch", value = "出库号", required = true, dataType = "String",paramType = "query")

    })
    @GetMapping("/query")
    @ControllerLog(description = "查询预检单")
    public ResponseEntity<Object> queryPreCheck(@RequestParam(name = "shopId") String shopId,
                                                @RequestParam(name = "bmbh") String bmbh,
                                                @RequestParam(name = "cch") String cch){
        return ResponseEntity.ok(preCheckConsumerService.queryPreCheck(shopId, bmbh, cch));
    }

    @ApiOperation(value = "生成预检单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "门店id", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "uuid", value = "前台传入的标识符 ", required = true, dataType = "String",paramType = "query")

    })
    @GetMapping("create")
    @ControllerLog(description = "生成预检单")
    public ResponseEntity<Object> createPreCheck(@RequestParam(name = "shopId") String shopId,
                                                 @RequestParam(name = "uuid") String uuid){
        return ResponseEntity.ok(preCheckConsumerService.createPreCheck(shopId, uuid));
    }

    @ApiOperation(value = "验货明细列表（差异列表）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "门店id", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "djdh", value = "大件单号 ", required = true, dataType = "String",paramType = "query")

    })
    @GetMapping("/inspect")
    @ControllerLog(description = "验货明细列表（差异列表）")
    public ResponseEntity<Object> inspectGoodsList(@RequestParam(name = "shopId") String shopId,
                                                   @RequestParam(name = "djdh") String djdh){
        return ResponseEntity.ok(preCheckConsumerService.inspectGoodsList(shopId, djdh));
    }

    @ApiOperation(value = "审核（不需要）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "门店id", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "djdh", value = "大件单号 ", required = true, dataType = "String",paramType = "query")

    })
    @GetMapping("/verify")
    @ControllerLog(description = "审核")
    public ResponseEntity<Object> verifyPreCheckList(@RequestParam(name = "shopId") String shopId,
                                                     @RequestParam(name = "djdh") String djdh){
        return ResponseEntity.ok(preCheckConsumerService.verifyPreCheckList(shopId, djdh));
    }

    @ApiOperation(value = "上传收货数据审核")
    @PostMapping(value = "/uploadDataToVerify")
    @ControllerLog(description = "上传收货数据审核")
    public ResponseEntity uploadDataToVerify(@RequestBody List<CollectGoodsModel> collectGoodsModels) {
        return ResponseEntity.ok(preCheckConsumerService.uploadData(collectGoodsModels));
    }

    @ApiOperation(value = "上传预检单数据返回验货明细/列表")
    @PostMapping(value = "/insertPreCheckData")
    @ControllerLog(description = "上传预检单数据返回验货明细/列表")
    public ResponseEntity insertPreCheckData(@RequestBody List<PreCheckModel> preCheckModel) {
        return ResponseEntity.ok(preCheckConsumerService.insertPreCheckData(preCheckModel));
    }


}
