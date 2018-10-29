package com.sanjiang.consumer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanjiang.annotation.ControllerLog;
import com.sanjiang.consumer.service.LoginConsumerService;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.core.ResultCode;
import com.sanjiang.provider.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * Created by byinbo on 2018/6/27.
 */
@Slf4j
@Api(tags = "LoginController", description = "登录接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LoginController {

    @Autowired
    private LoginConsumerService loginConsumerService;

    private String cookieName = "COOKIE";

    private boolean isServlet3Plus = isServlet3();


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名(必需)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码(必需)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "scbh", value = "商场编号(必需)", required = true, dataType = "String",paramType = "query")
    })
    @PostMapping
    @ControllerLog(description = "登录接口")
    public ResponseEntity login(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("scbh") String scbh){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        String ip = request.getRemoteAddr();
//        ip = "192.168.3.101";
        ResponseMessage responseMessage = null;
        try {
            responseMessage = loginConsumerService.login(username, password, scbh, ip);
            if (responseMessage.getCode() == ResultCode.SUCCESS.code()) {
                String sessionId = request.getSession().getId();
                String redisKey = "user:"+sessionId;
                ((User) responseMessage.getData()).setSessionId(sessionId);
                ValueOperations<String, String> operations = redisTemplate.opsForValue();

                operations.set(redisKey, objectMapper.writeValueAsString(responseMessage.getData()),
                        60*60L, TimeUnit.SECONDS);

                Cookie sessionCookie = new Cookie(cookieName, "");
                sessionCookie.setValue(sessionId);
                if (isServlet3Plus) {
                    sessionCookie.setHttpOnly(true);
                }
                sessionCookie.setSecure(request.isSecure());
                sessionCookie.setPath(cookiePath(request));
                sessionCookie.setMaxAge(60 * 60);

                response.addCookie(sessionCookie);

                //

//                request.getSession().setAttribute("user", objectMapper.writeValueAsString(responseMessage.getData()));
            }
        }catch (Exception e){
            log.info("登录失败：{}",e.getMessage());
            return ResponseEntity.ok("登录失败");
        }
        return ResponseEntity.ok(responseMessage);
    }

    @ApiOperation(value = "修改门店接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scbh", value = "商场编号(必需)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "ip", value = "ip(必需)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "mac", value = "mac地址(必需)", required = true, dataType = "String",paramType = "query")
    })
    @PostMapping("/config")
    @ControllerLog(description = "修改门店接口")
    public ResponseEntity loginConfig(@RequestParam("scbh") String scbh, @RequestParam("ip") String ip, @RequestParam("mac") String mac){
        return ResponseEntity.ok(loginConsumerService.loginConfig(scbh, ip, mac));
    }

    private boolean isServlet3() {
        try {
            ServletRequest.class.getMethod("startAsync");
            return true;
        } catch (NoSuchMethodException e) {
        }
        return false;
    }

    private static String cookiePath(HttpServletRequest request) {
        return request.getContextPath() + "/";
    }

}
