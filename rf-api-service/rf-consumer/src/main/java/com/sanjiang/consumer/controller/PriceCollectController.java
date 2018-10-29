package com.sanjiang.consumer.controller;

import com.sanjiang.annotation.ControllerLog;
import com.sanjiang.consumer.service.PriceCollectConsumerService;
import com.sanjiang.provider.domain.exhibitionwarehouse.ShopShelfProduct;
import com.sanjiang.provider.model.PriceCollectModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by byinbo on 2018/6/21.
 */
@Api(tags = "PriceCollectController", description = "采价接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/priceCollect", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PriceCollectController {

    @Autowired
    private PriceCollectConsumerService priceCollectConsumerService;

    @ApiOperation(value = "根据商品编号查询商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spbh", value = "商品编号(必需)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "scname", value = "对手商场(必需)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "czy", value = "操作员(必需)", required = true, dataType = "String",paramType = "query")

    })
    @GetMapping(value = "/queryBySpbh")
    @ControllerLog(description = "根据商品编号查询商品信息")
    public ResponseEntity query(@RequestParam(name = "spbh") String spbh, @RequestParam(name = "scname") String scname, @RequestParam(name = "czy") String czy) {

        return ResponseEntity.ok(priceCollectConsumerService.query(spbh,scname,czy));
    }

    @ApiOperation(value = "保存采价信息")
    @PostMapping(value = "/save")
    @ControllerLog(description = "保存采价信息")
    public ResponseEntity save(@RequestBody PriceCollectModel priceCollectModel,
                               HttpServletRequest httpServletRequest) {


        return ResponseEntity.ok(priceCollectConsumerService.save(priceCollectModel));
    }

    @ApiOperation(value = "统计当前用户采价记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "czy", value = "操作员", required = true, dataType = "String",paramType = "query")
    })
    @GetMapping(value = "/count")
    @ControllerLog(description = "统计当前用户采价记录")
    public ResponseEntity count(@RequestParam(name = "czy") String czy){
        return ResponseEntity.ok(priceCollectConsumerService.count(czy));
    }

}
