package com.sanjiang.consumer.controller;

/**
 * @author kimiyu
 * @date 2018/5/2 10:07
 */

import com.sanjiang.annotation.ControllerLog;
import com.sanjiang.consumer.model.ShopShelfProductModel;
import com.sanjiang.consumer.service.ShelfProductConsumerService;
import com.sanjiang.provider.domain.exhibitionwarehouse.ShopShelfProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 货架商品服务接口
 *
 * @author kimiyu
 * @date 2018/4/26 13:02
 */
@Api(tags = "ShelfProductController", description = "货架商品服务接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/shelfProducts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ShelfProductController {

    @Autowired
    private ShelfProductConsumerService shelfProductConsumerService;

    @ApiOperation(value = "保存货架商品信息")
    @PostMapping(value = "/save")
    @ControllerLog(description = "保存货架商品信息")
    public ResponseEntity save(@RequestBody ShopShelfProduct shopShelfProduct,
                               HttpServletRequest httpServletRequest) {


        return ResponseEntity.ok(shelfProductConsumerService.save(shopShelfProduct));
    }

    @ApiOperation(value = "货架商品信息列表")
    @PostMapping(value = "/list")
    @ControllerLog(description = "货架商品信息列表")
    public ResponseEntity list(@RequestBody ShopShelfProduct shopShelfProduct,
                               HttpServletRequest httpServletRequest) {

        return ResponseEntity.ok(shelfProductConsumerService.list(shopShelfProduct));
    }

    @ApiOperation(value = "根据流水号和货架号删除货架商品")
    @PostMapping(value = "/delById")
    @ControllerLog(description = "根据流水号和货架号删除货架商品")
    public ResponseEntity delProductById(@RequestBody ShopShelfProduct shopShelfProduct) {

        return ResponseEntity.ok(shelfProductConsumerService.delProductById(shopShelfProduct));
    }


    @ApiOperation(value = "根据流水号列表和货架号删除货架商品")
    @PostMapping(value = "/delByIdList")
    @ControllerLog(description = "根据流水号列表和货架号删除货架商品")
    public ResponseEntity delProductByIdList(@RequestBody ShopShelfProductModel ShopShelfProductModel) {

        return ResponseEntity.ok(shelfProductConsumerService.delProductByIdList(ShopShelfProductModel));
    }

    @ApiOperation(value = "根据层号和货架号删除货架商品")
    @PostMapping(value = "/delByLayerNumber")
    @ControllerLog(description = "根据层号和货架号删除货架商品")
    public ResponseEntity delProductByLayerNumber(@RequestBody ShopShelfProduct shopShelfProduct) {
        return ResponseEntity.ok(shelfProductConsumerService.delProductByLayerNumber(shopShelfProduct));
    }

    @ApiOperation(value = "根据管理编号获取商品在货架的位置列表")
    @PostMapping(value = "/getLocations")
    @ControllerLog(description = "根据管理编号获取商品在货架的位置列表")
    public ResponseEntity getLocationByErpGoodsId(@RequestBody ShopShelfProduct shopShelfProduct) {
        return ResponseEntity.ok(shelfProductConsumerService.getByLocation(shopShelfProduct));
    }

}
