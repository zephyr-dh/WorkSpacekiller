package com.sanjiang.provider.service.impl.login;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanjiang.constrants.DefaultValue;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.core.ResultCode;
import com.sanjiang.provider.Dao.LoginDao;
import com.sanjiang.provider.model.User;
import com.sanjiang.provider.service.login.LoginServie;
import com.sanjiang.provider.util.ConvertExceptionTip;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Created by byinbo on 2018/6/27.
 */
@Service(
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}",
        version = "1.0.0",
        timeout = 50000
)
@Slf4j
public class LoginServiceImpl implements LoginServie{
    @Autowired
    private LoginDao loginDao;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public ResponseMessage login(String username, String password, String scbh, String ip) {
        HikariDataSource hikariDataSource = loginDao.customDataSource(scbh);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
        try {
            String result = loginDao.execLoginValid(namedParameterJdbcTemplate,username, password,scbh, ip);
            if(result.equals("0")){
                User user = new User();
                user.setUsername(username);
                user.setPassword(null);
                user.setScbh(scbh);
                user.setIp(ip);
                return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),result,user);
            }else {
                return ResponseMessage.responseMessage(ResultCode.FAIL.code(),result,null);
            }
        }catch (Exception e){
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), ConvertExceptionTip.convert(ExceptionUtils.getMessage(e)), null);
        }finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage loginConfig(String scbh, String ip, String mac) {
        HikariDataSource hikariDataSource = loginDao.customDataSource(scbh);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
        try {
            loginDao.execLoginConfig(namedParameterJdbcTemplate,scbh, ip, mac);
        }catch (Exception e){
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(e));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), ConvertExceptionTip.convert(ExceptionUtils.getMessage(e)), null);
        }finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
        return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(),"成功",null);
    }
}
