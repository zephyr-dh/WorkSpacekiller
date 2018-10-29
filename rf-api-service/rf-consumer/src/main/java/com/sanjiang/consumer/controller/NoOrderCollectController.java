package com.sanjiang.consumer.controller;

import com.sanjiang.annotation.ControllerLog;
import com.sanjiang.consumer.service.NoOrderCollectConsumerService;
import com.sanjiang.provider.model.NoOrderCollectGoods;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by byinbo on 2018/6/25.
 */
@Api(tags = "NoOrderCollectController", description = "无订单收货接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/NoOrderCollect", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class NoOrderCollectController {

    @Autowired
    private NoOrderCollectConsumerService noOrderCollectConsumerService;

    @ApiOperation(value = "获取供应商列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scbh", value = "门店id(必需)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "bmbh", value = "供应商编号", required = false, dataType = "String",paramType = "query")
    })
    @GetMapping("/getSuppliers")
    @ControllerLog(description = "获取供应商列表")
    public ResponseEntity getSuppliers(@RequestParam("scbh") String scbh, @RequestParam(name = "bmbh", required = false) String bmbh ){
        return ResponseEntity.ok(noOrderCollectConsumerService.getSuppliers(scbh, bmbh));
    }

    @ApiOperation(value = "根据商品编号获取商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scbh", value = "门店id(必需)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "spbh", value = "商品编号(必需)", required = true, dataType = "String",paramType = "query")

    })
    @GetMapping("/getProductBySpbh")
    @ControllerLog(description = "根据商品编号获取商品信息")
    public ResponseEntity getProductBySpbh(@RequestParam("scbh") String scbh, @RequestParam("spbh") String spbh){
        return ResponseEntity.ok(noOrderCollectConsumerService.getProductBySpbh(scbh, spbh));
    }

    @ApiOperation(value = "保存无订单收货商品信息")
    @PostMapping("/save")
    @ControllerLog(description = "保存无订单收货商品信息")
    public ResponseEntity save(@RequestBody List<NoOrderCollectGoods> noOrderCollectGoodsList){
        return ResponseEntity.ok(noOrderCollectConsumerService.save(noOrderCollectGoodsList));
    }
}
