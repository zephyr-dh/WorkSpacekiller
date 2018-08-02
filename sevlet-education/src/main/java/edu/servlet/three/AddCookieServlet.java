package edu.servlet.three;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddCookieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Cookie c = new Cookie("username", "Sally");
		// 设置生存时间
		c.setMaxAge(40);
		response.addCookie(c);

		String city = URLEncoder.encode("北京", "utf-8");
		Cookie c2 = new Cookie("city", city);
		response.addCookie(c2);

	}

}
