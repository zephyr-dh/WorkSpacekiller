package com.sanjiang.consumer.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanjiang.consumer.config.session.SessionConfig;
import com.sanjiang.core.CommonCode;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.core.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by byinbo on 2018/6/27.
 */
@Aspect
@Component
@Slf4j
public class LoginFilter {

    @Autowired
    private SessionConfig sessionConfig;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisOperationsSessionRepository redisOperationsSessionRepository;

    @Pointcut("execution(* com.sanjiang.consumer.controller.*.*(..)) && !execution(* com.sanjiang.consumer.controller.LoginController.*(..))")
    public void logForController() {
        // 对service包里的类进行拦截
    }

    @Around(value = "logForController()")
    public ResponseEntity around(ProceedingJoinPoint pjp){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //读取session中的用户
//        String sessionId = request.getHeader(CommonCode.AUTHTOKEN.value());
        String sessionId = sessionConfig.getSessionId();
        log.info("sessionId={}",sessionId);
        String redisKey = "user:"+sessionId;
//        Session session = redisOperationsSessionRepository.getSession(sessionId);

        ValueOperations<String, String> operations = this.redisTemplate.opsForValue();
        String value = operations.get(redisKey);
        ResponseEntity responseEntity = null;
        if (StringUtils.isNotEmpty(value)) {
            try{
                responseEntity  = (ResponseEntity) pjp.proceed();
            } catch (Throwable ex) {
                log.info("异常信息：{}",ex.getMessage());
            }
        }else{
            responseEntity = ResponseEntity.ok(ResponseMessage.responseMessage(ResultCode.FAIL.code(),"未登录", null));
        }
        return responseEntity;

    }

}
