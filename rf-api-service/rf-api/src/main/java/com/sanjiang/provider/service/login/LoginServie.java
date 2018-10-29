package com.sanjiang.provider.service.login;

import com.sanjiang.core.ResponseMessage;

/**
 * Created by byinbo on 2018/6/27.
 */
public interface LoginServie {

    ResponseMessage login(String username, String password, String scbh, String ip);

    ResponseMessage loginConfig(String scbh, String ip, String mac);
}
