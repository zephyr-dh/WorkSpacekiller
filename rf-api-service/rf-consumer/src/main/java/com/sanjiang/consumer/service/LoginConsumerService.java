package com.sanjiang.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.provider.service.login.LoginServie;
import org.springframework.stereotype.Service;

/**
 * Created by byinbo on 2018/6/27.
 */
@Service
public class LoginConsumerService {

    @Reference(
            application = "${dubbo.application.id}",
            registry = "${dubbo.registry.id}",
//            url = "dubbo://193.0.1.229:20880",
            version = "1.0.0",
            timeout = 50000
    )
    private LoginServie loginServie;

    public ResponseMessage login(String username, String password, String scbh, String ip){
        return loginServie.login(  username,  password,  scbh,  ip);
    }

    public ResponseMessage loginConfig(String scbh, String ip, String mac){
        return loginServie.loginConfig(scbh,ip,mac);
    }

}
