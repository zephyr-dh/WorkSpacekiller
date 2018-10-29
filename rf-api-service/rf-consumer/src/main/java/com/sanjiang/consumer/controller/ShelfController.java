package com.sanjiang.consumer.controller;

import com.sanjiang.annotation.ControllerLog;
import com.sanjiang.consumer.service.SessionService;
import com.sanjiang.consumer.service.ShelfConsumerService;
import com.sanjiang.provider.domain.exhibitionwarehouse.ShopShelfNumber;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 货架服务接口
 *
 * @author kimiyu
 * @date 2018/4/26 13:02
 */
@Slf4j
@Api(tags = "ShelfController", description = "货架服务接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/shelfs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ShelfController {

    @Autowired
    private ShelfConsumerService shelfConsumerService;

    @Autowired
    private SessionService sessionService;

    /**
     * 获取货架参数列表
     *
     * @param shopId
     * @param httpServletRequest
     * @return
     */
    @ApiOperation(value = "获取货架参数列表")
    @GetMapping(value = "/getConstants")
    @ControllerLog(description = "获取货架参数列表")
    public ResponseEntity getConstants(@RequestParam(value = "shopId", defaultValue = "00023") String shopId,
                                       HttpServletRequest httpServletRequest) {

        String deviceId = httpServletRequest.getHeader("deviceId");
        if (StringUtils.isEmpty(deviceId)) {
            // 增加一个默认的设备ID号
            deviceId = UUID.randomUUID().toString();
        }
//        String token = httpServletRequest.getHeader(CommonCode.AUTHTOKEN.value());
//        // 认证未通过
//        User user = sessionService.checkLogin(token);
//        if (null == user) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(ResponseMessage.responseMessage(ResultCode.FAIL.code(), "用户未登录!", null));
//        }

//        String workerId = user.getWorkerId();

//        log.info("workerId:{},deviceId:{}", workerId, deviceId);

        return ResponseEntity.ok(shelfConsumerService.getConstatns(shopId));
    }

    /**
     * 获取货架列表
     *
     * @param shopId
     * @param httpServletRequest
     * @return
     */
    @ApiOperation(value = "获取货架列表")
    @GetMapping(value = "/getShelfs")
    @ControllerLog(description = "获取货架列表")
    public ResponseEntity getShelfs(@RequestParam(value = "shopId", defaultValue = "00023") String shopId,
                                    HttpServletRequest httpServletRequest) {

        String deviceId = httpServletRequest.getHeader("deviceId");
        if (StringUtils.isEmpty(deviceId)) {
            // 增加一个默认的设备ID号
            deviceId = UUID.randomUUID().toString();
        }
//        String token = httpServletRequest.getHeader(CommonCode.AUTHTOKEN.value());
//        // 认证未通过
//        User user = sessionService.checkLogin(token);
//        if (null == user) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(ResponseMessage.responseMessage(ResultCode.FAIL.code(), "用户未登录!", null));
//        }

//        String workerId = user.getWorkerId();

//        log.info("workerId:{},deviceId:{}", workerId, deviceId);

        return ResponseEntity.ok(shelfConsumerService.getShelfs(shopId));
    }


    /**
     * 保存货架信息
     *
     * @param shopShelfNumber
     * @return
     */
    @ApiOperation(value = "保存货架信息")
    @PostMapping(value = "/save")
    @ControllerLog(description = "保存货架信息")
    public ResponseEntity save(@RequestBody ShopShelfNumber shopShelfNumber) {
        return ResponseEntity.ok(shelfConsumerService.saveShelfNumber(shopShelfNumber));
    }


    /**
     * 根据门店和货架号返回货架信息
     *
     * @param shopId
     * @param shelfNumber
     * @return
     */
    @ApiOperation(value = "根据门店和货架号返回货架信息")
    @GetMapping(value = "/getShelf")
    @ControllerLog(description = "根据门店和货架号返回货架信息")
    public ResponseEntity getShelf(@RequestParam(value = "shopId", defaultValue = "00023") String shopId,
                                   @RequestParam String shelfNumber,
                                   HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(shelfConsumerService.getShelf(shopId, shelfNumber));
    }


    /**
     * 根据门店和货架号返回货架信息
     *
     * @param shopId
     * @param shelfNumber
     * @return
     */
    @ApiOperation(value = "根据门店和货架号清空货架信息")
    @GetMapping(value = "/cleanShelf")
    @ControllerLog(description = "根据门店和货架号清空货架信息")
    public ResponseEntity cleanShelf(@RequestParam(value = "shopId", defaultValue = "00023") String shopId,
                                     @RequestParam String shelfNumber,
                                     HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(shelfConsumerService.cleanShelf(shopId, shelfNumber));
    }
}
