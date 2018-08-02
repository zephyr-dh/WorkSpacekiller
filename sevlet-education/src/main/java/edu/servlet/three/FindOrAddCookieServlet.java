package edu.servlet.three;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FindOrAddCookieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			boolean flag = false;
			for (Cookie c : cookies) {
				String name = c.getName();
				if ("cart".equals(name)) {
					// �ҵ��ˣ������
					out.println(c.getValue());
					flag = true;
				}
			}
			if (!flag) {
				// û���ҵ��������
				Cookie c = new Cookie("cart", "1,2,3");
				response.addCookie(c);
			}
		} else {
			// �Ҳ����κε�cookie,�����
			Cookie c = new Cookie("cart", "1,2,3");
			response.addCookie(c);
		}
	}

}