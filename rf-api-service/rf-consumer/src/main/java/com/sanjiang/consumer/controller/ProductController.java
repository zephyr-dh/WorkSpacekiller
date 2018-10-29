package com.sanjiang.consumer.controller;

import com.sanjiang.annotation.ControllerLog;
import com.sanjiang.consumer.service.ProductConsumerService;
import com.sanjiang.core.CommonCode;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.core.ResultCode;
import com.sanjiang.provider.domain.ProductDomain;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 商品服务
 *
 * @author kimiyu
 * @date 2018/4/17 11:34
 */
@Api(tags = "ProductController", description = "商品服务接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProductController {

    @Autowired
    private ProductConsumerService productConsumerService;

    /**
     * 商品查询
     *
     * @param shopId
     * @param spbm
     * @param httpServletRequest
     * @return
     */
    @ApiOperation(value = "根据门店和条码商品查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "门店id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "spbm", value = "商品编码", required = true, dataType = "String")
    })
    @GetMapping(value = "/search")
    @ControllerLog(description = "根据门店和条码商品查询w")
    public ResponseEntity<Object> search(@RequestParam(value = "shopId", defaultValue = "00023") String shopId,
                                         @RequestParam("spbm") String spbm,
                                         HttpServletRequest httpServletRequest) {

        String deviceId = httpServletRequest.getHeader("deviceId");
        if (StringUtils.isEmpty(deviceId)) {
            // 增加一个默认的设备ID号
            deviceId = UUID.randomUUID().toString();
        }
        String workerId = httpServletRequest.getHeader("workerId");
        String token = httpServletRequest.getHeader(CommonCode.AUTHTOKEN.value());
        // 认证未通过
//        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(workerId)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        Boolean validToken = TokenUtil.checkToken(token, workerId, 1800L);
//        if (!validToken) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }


        if (StringUtils.isEmpty(shopId)
                || StringUtils.isEmpty(spbm)
                || StringUtils.isEmpty(deviceId)) {
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.FAIL.code(), "参数校验错误", null));
        }


        List<ProductDomain> productDomains = productConsumerService.getProducts(shopId, spbm, deviceId, workerId);

        if (CollectionUtils.isEmpty(productDomains)) {
            return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.FAIL.code(), "没有查询到商品", null));
        }
        return ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "", productDomains));
    }

}
