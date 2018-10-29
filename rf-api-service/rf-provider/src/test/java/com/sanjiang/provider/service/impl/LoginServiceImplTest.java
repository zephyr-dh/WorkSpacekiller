package com.sanjiang.provider.service.impl;

import com.sanjiang.provider.service.BaseTest;
import com.sanjiang.provider.service.login.LoginServie;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by byinbo on 2018/6/28.
 */
public class LoginServiceImplTest extends BaseTest{

    @Autowired
    private LoginServie loginServie;

    @Test
    public void test(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        System.out.println(loginServie.login("02410","123","00003","192.168.3.101"));
    }
}
