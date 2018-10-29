package com.sanjiang.provider.util;

import com.sanjiang.auth.domain.ClientInfo;
import com.sanjiang.auth.utils.JWTUtils;

/**
 * @author kimiyu
 * @date 2018/4/21 13:47
 */
public class TokenUtil {

    private TokenUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 校验token的有效性
     *
     * @param token
     * @param username
     * @param expTime
     * @return
     */
    public static boolean checkToken(String token, String username, Long expTime) {
        JWTUtils jwtUtils = new JWTUtils();
        return jwtUtils.validateToken(token, genClientInfoByLogin(username, expTime));
    }

    /**
     * 创建登录客户端信息
     *
     * @param username 用户名
     * @param expTime  超时时间
     * @return
     */
    private static ClientInfo genClientInfoByLogin(String username, Long expTime) {
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setPwd(username);
        clientInfo.setClientId(username);
        clientInfo.setExp(expTime * 1000L);
        clientInfo.setClientId(username);
        clientInfo.setSign("$2a$10$0CYQ5fn0XZjD9zRgGZGJ1.712fo8UBBsopfGjDPQyC8Q/3CqPfFNm");
        return clientInfo;
    }
}
