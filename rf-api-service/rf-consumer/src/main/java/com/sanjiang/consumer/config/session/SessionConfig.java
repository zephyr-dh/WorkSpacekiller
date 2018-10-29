package com.sanjiang.consumer.config.session;

import com.sanjiang.core.CommonCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Created by byinbo on 2018/6/28.
 */
@Component
@Slf4j
public class SessionConfig {

    private String cookieName = "COOKIE";

    static final String DEFAULT_ALIAS = "0";

    static final String DEFAULT_SESSION_ALIAS_PARAM_NAME = "_s";

    private String sessionParam = DEFAULT_SESSION_ALIAS_PARAM_NAME;

    private Pattern ALIAS_PATTERN = Pattern.compile("^[\\w-]{1,50}$");



    public String getSessionId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // header part
        String sessionId = request.getHeader(CommonCode.AUTHTOKEN.value());
        if (sessionId != null && !sessionId.isEmpty()) {
            return sessionId;
        }

        // cookie part
        Map<String, String> sessionIds = getSessionIds(request);
        sessionIds.forEach((k,v)-> log.info("k={},v={}",k,v));
        String sessionAlias = getCurrentSessionAlias(request);
        return sessionIds.get(sessionAlias);
    }

    public Map<String, String> getSessionIds(HttpServletRequest request) {
        Cookie session = getCookie(request, cookieName);
        String sessionCookieValue = session == null ? "" : session.getValue();
        Map<String, String> result = new LinkedHashMap<String, String>();
        StringTokenizer tokens = new StringTokenizer(sessionCookieValue, " ");
        if (tokens.countTokens() == 1) {
            result.put(DEFAULT_ALIAS, tokens.nextToken());
            return result;
        }
        while (tokens.hasMoreTokens()) {
            String alias = tokens.nextToken();
            if (!tokens.hasMoreTokens()) {
                break;
            }
            String id = tokens.nextToken();
            result.put(alias, id);
        }
        return result;
    }

    private static Cookie getCookie(HttpServletRequest request, String name) {
        if (request == null) {
            throw new IllegalArgumentException("request cannot be null");
        }
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public String getCurrentSessionAlias(HttpServletRequest request) {
        if (sessionParam == null) {
            return DEFAULT_ALIAS;
        }
        String u = request.getParameter(sessionParam);
        if (u == null) {
            return DEFAULT_ALIAS;
        }
        if (!ALIAS_PATTERN.matcher(u).matches()) {
            return DEFAULT_ALIAS;
        }
        return u;
    }
}
