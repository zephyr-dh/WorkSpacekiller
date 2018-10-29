package com.sanjiang.consumer.service;

import com.sanjiang.auth.domain.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

/**
 * session服务
 *
 * @author kimiyu
 * @date 2018/5/5 15:11
 */
@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    /**
     * 检查用户是否登录
     *
     * @param sessionId
     * @return
     */
    public User checkLogin(String sessionId) {
        if (StringUtils.isEmpty(sessionId)) {
            return null;
        }

        Session session = sessionRepository.getSession(sessionId);
        if (null == session) {
            return null;
        }

        return session.getAttribute("user:");

    }
}
