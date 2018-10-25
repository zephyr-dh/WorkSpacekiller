package io.oacy.education.springbootnoob.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CustomAspect {

//    @Around("")
//    public Object method(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
//        System.out.println("=====Aspect处理=======");
//        Object[] args = proceedingJoinPoint.getArgs();
//        for (Object arg : args) {
//            System.out.println("参数为:" + arg);
//        }
//        long start = System.currentTimeMillis();
//        try {
//
//            Object object= proceedingJoinPoint.proceed();
//            System.out.println("Aspect 耗时:" + (System.currentTimeMillis() - start));
//            return object;
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        return null;
//    }
}
