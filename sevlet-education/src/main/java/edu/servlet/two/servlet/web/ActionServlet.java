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
		// ��������Ĳ���ֵ
		request.setCharacterEncoding("utf-8");

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		// ���������Դ·��
		String uri = request.getRequestURI();
		// ����������Դ·��
		String action = uri.substring(uri.lastIndexOf("/"), uri.lastIndexOf("."));
		System.out.println("action:" + action);

		// ��������·������Ӧ�Ĵ���
		if ("/list".equals(action)) {

			/*
			 * ����session��֤
			 */
			HttpSession session = request.getSession();

			Object obj = session.getAttribute("user");
			if (obj == null) {
				// û�е�¼,��ת����¼ҳ��
				response.sendRedirect("login.jsp");
				return;
			}

			// ʹ��DAO��ѯ���ݿ⣬�������û���Ϣ��ѯ����
			try {
				UserDao dao = new UserDao();
				List<User> users = dao.findAll();
				// ת����jsp,��jsp��չ��(�û��б�)
				// step1.������
				request.setAttribute("users", users);
				// step2.���ת����
				RequestDispatcher rd = request.getRequestDispatcher("listUser.jsp");
				// step3.ת��
				rd.forward(request, response);

			} catch (Exception e) {
				e.printStackTrace();
				out.println("ϵͳ��æ���Ժ�����");
			}
		} else if ("/add".equals(action)) {

			// ��ȡ�û���Ϣ
			String uname = request.getParameter("uname");
			String pwd = request.getParameter("pwd");
			String phone = request.getParameter("phone");

			System.out.println("uname:" + uname + " pwd:" + pwd + " phone:" + phone);

			// ��Ա����Ϣ���뵽���ݿ�
			try {
				UserDao dao = new UserDao();
				User user = new User();
				user.setUname(uname);
				user.setPwd(pwd);
				user.setPhone(phone);
				dao.save(user);
				// �ض����û��б�
				response.sendRedirect("list.do");
			} catch (Exception e) {
				// step1.�ȼ���־(�����ֳ�)
				e.printStackTrace();
				/*
				 * step2.���쳣�ܷ�ָ���������ܹ� �ָ������磬���ݿ����ֹͣ�ˣ��� �����쳣���ǳ�֮Ϊϵͳ�쳣������ ��ʾ�û��Ժ����ԡ�����ܹ��ָ��� �������ָ���
				 */
				out.println("ϵͳ��æ���Ժ�����");
			}

		} else if ("/del".equals(action)) {
			// ��ȡҪɾ�����û���id
			String id = request.getParameter("id");
			// ����dao����ķ�����ɾ��ָ��id���û�
			UserDao dao = new UserDao();
			try {
				dao.delete(Integer.parseInt(id));
				// �ض����û��б�
				response.sendRedirect("list.do");
			} catch (Exception e) {
				e.printStackTrace();
				out.println("ϵͳ��æ���Ժ�����");
			}
		} else if ("/login".equals(action)) {
			/*
			 * �ȱȽ���֤���Ƿ���ȷ
			 */
			// ����û��ύ����֤��
			String number1 = request.getParameter("number");
			// ������Ȱ󶩵�session�����ϵ���֤��
			HttpSession session = request.getSession();
			String number2 = (String) session.getAttribute("number");
			if (!number1.equals(number2)) {
				// ��֤�벻ƥ��,��Ҫ��ʾ�û�
				request.setAttribute("number_error", "��֤�����");
				request.getRequestDispatcher("login.jsp").forward(request, response);
				return;
			}

			// ��ȡ�û���������
			String uname = request.getParameter("uname");
			String pwd = request.getParameter("pwd");

			UserDao dao = new UserDao();
			try {
				// �����û�������ѯ���ݿ�
				User user = dao.findByUsername(uname);

				if (user != null && user.getPwd().equals(pwd)) {
					// �ҵ���ƥ��ļ�¼�����¼�ɹ���

					// ��һЩ���ݰ󶩵�session������,
					// Ϊsession��֤��׼����

					session.setAttribute("user", user);

					response.sendRedirect("list.do");
				} else {
					// û���ҵ�ƥ���¼�����¼ʧ��
					request.setAttribute("login_failed", "�û������������");
					request.getRequestDispatcher("login.jsp").forward(request, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
				out.println("ϵͳ��æ���Ժ�����");
			}
		}

	}

}
