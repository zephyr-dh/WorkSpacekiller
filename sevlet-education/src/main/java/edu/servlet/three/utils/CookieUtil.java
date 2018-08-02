package edu.servlet.three.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * Cookie工具类，提供了 cookie的基本操作。
 */
public class CookieUtil {
	/**
	 * 
	 * 添加Cookie
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static void addCookie(String name, String value, int age, String path, HttpServletResponse response)
			throws UnsupportedEncodingException {
		Cookie c = new Cookie(name, URLEncoder.encode(value, "utf-8"));
		c.setMaxAge(age);
		c.setPath(path);
		response.addCookie(c);
	}

	/**
	 * 依据cookie的名称，读取cookie的值。 如果找不到对应的cookie,返回null。
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static String findCookie(String name, HttpServletRequest request) throws UnsupportedEncodingException {
		String value = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (name.equals(c.getName())) {
					value = URLDecoder.decode(c.getValue(), "utf-8");
				}
			}
		}
		return value;
	}

	/**
	 * 删除cookie
	 */
	public static void deleteCookie(String name, HttpServletResponse response, String path) {
		Cookie c = new Cookie(name, "");
		c.setMaxAge(0);
		c.setPath(path);
		response.addCookie(c);
	}

}