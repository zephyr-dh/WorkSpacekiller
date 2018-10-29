package com.sanjiang.consumer.controller;

import com.sanjiang.annotation.ControllerLog;
import com.sanjiang.consumer.service.PreWarnConsumerService;
import com.sanjiang.provider.service.goodsoutoftime.PreWarnShelfLifeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by byinbo on 2018/5/14.
 */
@Api(tags = "PreWarnShelfLifeController", description = "保质期预警接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/preWarnShelfLife", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PreWarnShelfLifeController {

    @Autowired
    private PreWarnConsumerService preWarnConsumerService;

    @ApiOperation(value = "保质期预警接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "report", value = "接口类型(必需): hzcx(汇总查询),yjdsc_fzm(展示组别),yjd_jc_fzm(按组汇总信息),yjd_jc_sp(组别对应的商品列表),yjd_dpjc(显示单品信息),yjd_sp_scrq(获取生产日期),yjd_sp_hjbh(获取货架号)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "shopId", value = "门店id(必需)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "jcrq", value = "生成日期: (例2018-05-11 10:26:04); report=yjd_jc_fzm,yjd_jc_sp", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "fzm", value = "组别范围:(例50) report=yjd_jc_sp", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "spbh", value = "商品编号/条码:(例5110712，6901209212215) report=yjd_dpjc,yjd_sp_scrq,yjd_sp_hjbh", required = false, dataType = "String",paramType = "query")
    })
    @GetMapping
    @ControllerLog(description = "保质期预警接口")
    public ResponseEntity<Object> preWarn(@RequestParam(name = "report") String report,
                                          @RequestParam(name = "shopId") String shopId,
                                          @RequestParam(name = "jcrq", required = false) String jcrq,
                                          @RequestParam(name = "fzm", required = false) String fzm,
                                          @RequestParam(name = "spbh", required = false) String spbh){
        return ResponseEntity.ok(preWarnConsumerService.getPreWarn(report,shopId,jcrq,fzm,spbh));
    }

    @ApiOperation(value = "生成预警单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fzm", value = "组别数组(必需): 40,50", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "shopId", value = "门店id(必需)", required = true, dataType = "String",paramType = "query"),
    })
    @GetMapping("/create")
    @ControllerLog(description = "生成预警单")
    public ResponseEntity<Object> preWarnCreate(@RequestParam(name = "fzm") String[] fzm,
                                                @RequestParam(name = "shopId") String shopId){
        return ResponseEntity.ok(preWarnConsumerService.createPreWarn(shopId,fzm));
    }

    @ApiOperation(value = "商品检查")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "czlx", value = "操作类型: update,insert,delete", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "shopId", value = "门店id", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "glbh", value = "管理编码:5110712", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "scrq", value = "生成日期: (例2018-05-11)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "hjbh", value = "货架编号", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "clry", value = "操作员", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "jcrq", value = "预警单生成日期: (例2018-05-11 10:26:04)", required = false, dataType = "String",paramType = "query")
    })
    @GetMapping("/checkGoods")
    @ControllerLog(description = "商品检查")
    public ResponseEntity<Object> checkGoods(@RequestParam(name = "czlx") String czlx,
                                             @RequestParam(name = "shopId") String shopId,
                                             @RequestParam(name = "glbh") String glbh,
                                             @RequestParam(name = "scrq") String scrq,
                                             @RequestParam(name = "hjbh",required = false) String hjbh,
                                             @RequestParam(name = "clry",required = false) String clry,
                                             @RequestParam(name = "jcrq",required = false) String jcrq){
        return ResponseEntity.ok(preWarnConsumerService.checkGoods(czlx, shopId, glbh, scrq, hjbh, clry, jcrq));
    }
}
