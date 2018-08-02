package edu.servlet.three;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FindCookieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		// ��ȡ������˷��͹�����cookie
		Cookie[] cookies = request.getCookies();
		// �п���û���κ�cookie
		if (cookies != null) {
			for (Cookie c : cookies) {
				// ��ȡcookie������
				String name = c.getName();
				// ��ȡcookie��ֵ
				String value = URLDecoder.decode(c.getValue(), "utf-8");
				out.println(name + " " + value + "<br/>");
			}
		} else {
			out.println("û���ҵ��κ�cookie");
		}

	}

}