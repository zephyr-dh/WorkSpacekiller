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
			// ������û�а�����Ӧ��cookie,
			// �������Ӧ��cookie
			CookieUtil.addCookie("count", 1 + "", 24 * 60 * 60, request.getContextPath(), response);
			
			out.println("���ǵ�1�η���:"+request.getContextPath());
		} else {
			// �����а����˶�Ӧ��cookie,
			// �򽫴����ӣ������ҽ�cookie���¡�
			int i = Integer.parseInt(count) + 1;
			CookieUtil.addCookie("count", i + "", 24 * 60 * 60, request.getContextPath(), response);
			System.out.println("���ǵ�: " + i + " �η���:"+request.getContextPath());
			out.println("���ǵ�: " + i + " �η���:"+request.getContextPath());

		}

	}

}
