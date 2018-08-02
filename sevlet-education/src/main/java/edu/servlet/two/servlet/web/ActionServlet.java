package edu.servlet.two.servlet.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.servlet.two.dao.UserDao;
import edu.servlet.two.domain.User;

public class ActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 处理表单中文参数值
		request.setCharacterEncoding("utf-8");

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		// 获得请求资源路径
		String uri = request.getRequestURI();
		// 分析请求资源路径
		String action = uri.substring(uri.lastIndexOf("/"), uri.lastIndexOf("."));
		System.out.println("action:" + action);

		// 依据请求路径做对应的处理
		if ("/list".equals(action)) {

			/*
			 * 进行session验证
			 */
			HttpSession session = request.getSession();

			Object obj = session.getAttribute("user");
			if (obj == null) {
				// 没有登录,跳转到登录页面
				response.sendRedirect("login.jsp");
				return;
			}

			// 使用DAO查询数据库，将所有用户信息查询出来
			try {
				UserDao dao = new UserDao();
				List<User> users = dao.findAll();
				// 转发给jsp,由jsp来展现(用户列表)
				// step1.绑订数据
				request.setAttribute("users", users);
				// step2.获得转发器
				RequestDispatcher rd = request.getRequestDispatcher("listUser.jsp");
				// step3.转发
				rd.forward(request, response);

			} catch (Exception e) {
				e.printStackTrace();
				out.println("系统繁忙，稍后重试");
			}
		} else if ("/add".equals(action)) {

			// 读取用户信息
			String uname = request.getParameter("uname");
			String pwd = request.getParameter("pwd");
			String phone = request.getParameter("phone");

			System.out.println("uname:" + uname + " pwd:" + pwd + " phone:" + phone);

			// 将员工信息插入到数据库
			try {
				UserDao dao = new UserDao();
				User user = new User();
				user.setUname(uname);
				user.setPwd(pwd);
				user.setPhone(phone);
				dao.save(user);
				// 重定向到用户列表
				response.sendRedirect("list.do");
			} catch (Exception e) {
				// step1.先记日志(保留现场)
				e.printStackTrace();
				/*
				 * step2.看异常能否恢复，如果不能够 恢复（比如，数据库服务停止了，这 样的异常我们称之为系统异常），则 提示用户稍后重试。如果能够恢复， 则立即恢复。
				 */
				out.println("系统繁忙，稍后重试");
			}

		} else if ("/del".equals(action)) {
			// 读取要删除的用户的id
			String id = request.getParameter("id");
			// 调用dao对象的方法来删除指定id的用户
			UserDao dao = new UserDao();
			try {
				dao.delete(Integer.parseInt(id));
				// 重定向到用户列表
				response.sendRedirect("list.do");
			} catch (Exception e) {
				e.printStackTrace();
				out.println("系统繁忙，稍后重试");
			}
		} else if ("/login".equals(action)) {
			/*
			 * 先比较验证码是否正确
			 */
			// 获得用户提交的验证码
			String number1 = request.getParameter("number");
			// 获得事先绑订到session对象上的验证码
			HttpSession session = request.getSession();
			String number2 = (String) session.getAttribute("number");
			if (!number1.equals(number2)) {
				// 验证码不匹配,需要提示用户
				request.setAttribute("number_error", "验证码错误");
				request.getRequestDispatcher("login.jsp").forward(request, response);
				return;
			}

			// 读取用户名和密码
			String uname = request.getParameter("uname");
			String pwd = request.getParameter("pwd");

			UserDao dao = new UserDao();
			try {
				// 依据用户名，查询数据库
				User user = dao.findByUsername(uname);

				if (user != null && user.getPwd().equals(pwd)) {
					// 找到了匹配的记录，则登录成功。

					// 将一些数据绑订到session对象上,
					// 为session验证做准备。

					session.setAttribute("user", user);

					response.sendRedirect("list.do");
				} else {
					// 没有找到匹配记录，则登录失败
					request.setAttribute("login_failed", "用户名或密码错误");
					request.getRequestDispatcher("login.jsp").forward(request, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
				out.println("系统繁忙，稍后重试");
			}
		}

	}

}
