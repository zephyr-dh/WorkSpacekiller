package edu.servlet.three;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.servlet.three.utils.CookieUtil;

public class CountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		String count = CookieUtil.findCookie("count", request);
		if (count == null) {
			// 请求当中没有包含对应的cookie,
			// 则添加相应的cookie
			CookieUtil.addCookie("count", 1 + "", 24 * 60 * 60, request.getContextPath(), response);
			
			out.println("你是第1次访问:"+request.getContextPath());
		} else {
			// 请求当中包含了对应的cookie,
			// 则将次数加１，并且将cookie更新。
			int i = Integer.parseInt(count) + 1;
			CookieUtil.addCookie("count", i + "", 24 * 60 * 60, request.getContextPath(), response);
			System.out.println("你是第: " + i + " 次访问:"+request.getContextPath());
			out.println("你是第: " + i + " 次访问:"+request.getContextPath());

		}

	}

}
