package edu.servlet.one;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloWorldServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public HelloWorldServlet(){
		System.out.println("HelloServlet's "
				+ "Contructor");
	}

	/**
	 * override HttpServlet的service方法。
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("HelloServlet's " + "service()");

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateInfo = sdf.format(date);

		/*
		 * 读取请求参数值
		 */
		String name = request.getParameter("name");
		String age = request.getParameter("age");

		/*
		 * 告诉浏览器，服务器返回给浏览器的 数据类型。
		 */
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();

		out.println("<h1>Hello " + name + " " + age + "</h1>");
		out.println("<h1>" + dateInfo + "</h1>");

		out.close();
	}

}
