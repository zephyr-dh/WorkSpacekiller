package edu.servlet.two.servlet.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CheckcodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("CheckcodeServlet's service()");
		/*
		 * ��ͼ
		 */
		// step1.����һ���ڴ�ӳ����󣨻�����
		BufferedImage image = new BufferedImage(80, 30, BufferedImage.TYPE_INT_RGB);
		// step2.��û���
		Graphics g = image.getGraphics();
		// step3.����������ɫ
		g.setColor(new Color(255, 255, 255));
		// step4.���������ñ�����ɫ
		g.fillRect(0, 0, 80, 30);
		// step5.���¸���������ɫ
		Random r = new Random();

		// step6.����number(��֤��)
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 5; i++) {
			String str = getNumber(1);
			sb.append(str);
			// ���һ��������ַ���С
			int h = (int) (30 * 0.3 + 30 * 0.7 * r.nextDouble());
			// ��������
			g.setFont(new Font(null, Font.ITALIC | Font.BOLD, h));
			g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			// ����֤����ӵ�ͼƬ����
			g.drawString(str, 80 / 5 * i, h);
		}
		String number = sb.toString();
		System.out.println("number:" + number);

		// step7.����֤��󶩵�session������
		HttpSession session = request.getSession();
		session.setAttribute("number", number);

		// step8.��һЩ������
		for (int i = 0; i < 8; i++) {
			g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			g.drawLine(r.nextInt(80), r.nextInt(30), r.nextInt(80), r.nextInt(30));
		}

		/*
		 * ��ͼƬѹ�������͸��������
		 */
		// step1.����MIMI����(�����������
		// ���������ص�����������ʲô�������
		// ���ص���һ��jpeg��ʽ��ͼƬ)��
		response.setContentType("image/jpeg");
		// step2.����ֽ��������
		OutputStream output = response.getOutputStream();
		// step3.ѹ��ͼƬ�����
		javax.imageio.ImageIO.write(image, "jpeg", output);

	}

	/*
	 * ����һ�����ȹ̶�(size���ַ�),��������� A~Z,0~9��ѡȡ���ַ���ɵ���֤��
	 */
	private String getNumber(int size) {
		Random r = new Random();
		String number = "";
		String chars = "ABCDEFGHJIKLMNOPQRSTUVWXYZ0123456789";
		for (int i = 0; i < size; i++) {
			number += chars.charAt(r.nextInt(chars.length()));
		}
		return number;
	}

}
