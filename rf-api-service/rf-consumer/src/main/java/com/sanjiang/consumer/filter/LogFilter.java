package com.sanjiang.consumer.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanjiang.annotation.ControllerLog;
import com.sanjiang.auth.domain.User;
import com.sanjiang.consumer.config.session.SessionConfig;
import com.sanjiang.consumer.service.SessionService;
import com.sanjiang.core.CommonCode;
import com.sanjiang.message.domain.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author kimiyu
 * @date 2018/4/16 10:25
 */
@Aspect
@Slf4j
@Component
public class LogFilter {

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionConfig sessionConfig;

//    @Reference(
//            application = "message-api-provider",
//            registry = "message-api-registry",
//            group = "sanjiang-log",
//            version = "1.0.0",
//            timeout = 3000
//    )
//    private LogService logService;

    @Pointcut("execution(* com.sanjiang.consumer.service.*.*(..))")
    public void logForService() {
        // 对service包里的类进行拦截
    }

    @Pointcut("@annotation(com.sanjiang.annotation.ControllerLog)")
    public void logForController() {
    }

    @Around(value = "logForService()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        //获取用户请求方法的参数并序列化为JSON格式字符串
        StringBuilder params = new StringBuilder();
        String methodName = "";
        String ip = "";
        Long startTime = System.currentTimeMillis();
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            // 获取IP
            ip = request.getRemoteAddr();
            // 获取方法名
            methodName = (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    params.append(objectMapper.writeValueAsString(joinPoint.getArgs()[i]))
                            .append(";");
                }
            }
            Object result = joinPoint.proceed();
            log.info("请求地址：{},rest请求方法名：{};请求参数：{};消耗时间：{}", ip, methodName, params.toString(), System.currentTimeMillis() - startTime);
            return result;
        } catch (Throwable ex) {
            log.info("请求地址:{}；异常方法:{}；异常代码:{}；异常信息:{}；参数:{}；", ip, methodName, ex.getClass().getName(), ex.getMessage(), params);
            return null;
        }
    }


    @Before("logForController()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            //保存数据库
            saveSystemLog(joinPoint, request);
        } catch (Exception e) {
            //记录本地异常日志
            log.error("异常信息:{}", e.getMessage());
        }
    }


    /**
     * 保存系统日志
     *
     * @param joinPoint
     */
    private void saveSystemLog(JoinPoint joinPoint, HttpServletRequest httpServletRequest) throws Exception {
        SystemLog systemLog = new SystemLog();
        String sessionId = httpServletRequest.getHeader(CommonCode.AUTHTOKEN.value());
        String deviceId = httpServletRequest.getHeader(CommonCode.DEVICEID.value());
        if (!StringUtils.isEmpty(deviceId)) {
            systemLog.setDeviceId(deviceId);
        }
        //读取session中的用户
//        String sessionId = sessionConfig.getSessionId();
        String redisKey = "user:"+sessionId;
        ValueOperations<String, String> operations = this.redisTemplate.opsForValue();
        String value = operations.get(redisKey);//        User user = sessionService.checkLogin(token);
        String username = "admin";
        if (!StringUtils.isEmpty(value)) {
            User user = objectMapper.readValue(value, User.class);
            username = user.getUsername();
        }
        systemLog.setId(UUID.randomUUID().toString());
        systemLog.setMethodName(joinPoint.getSignature().getName());
        systemLog.setClassName(joinPoint.getTarget().getClass().getName());
        systemLog.setMethodDescrition(getControllerMethodDescription(joinPoint));
        systemLog.setIp(httpServletRequest.getRemoteAddr());
        systemLog.setOperateTime(LocalDateTime.now());
        systemLog.setAppName(appName);
        systemLog.setOperator(username);
        systemLog.setLogType(0);
        //获取用户请求方法的参数并序列化为JSON格式字符串
        StringBuilder params = new StringBuilder();
        Object[] args = joinPoint.getArgs();
        int length = args.length;
        if (length > 0) {
            for (int i = 0; i < length - 1; i++) {
                params.append(objectMapper.writeValueAsString(joinPoint.getArgs()[i]))
                        .append(";");
            }
        }
        systemLog.setRequestParams(params.toString());
        systemLog.setRequestPath(httpServletRequest.getServletPath());
        systemLog.setRequestUri(httpServletRequest.getRequestURI());

        try {
            log.info("接收到的系统日志：{}", objectMapper.writeValueAsString(systemLog));
        } catch (IOException ex) {

        }

//        logService.saveSystemLog(systemLog);
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    private static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] classes = method.getParameterTypes();
                if (classes.length == arguments.length) {
                    description = method.getAnnotation(ControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }
}



