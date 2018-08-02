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
		 * 绘图
		 */
		// step1.创建一个内存映像对象（画布）
		BufferedImage image = new BufferedImage(80, 30, BufferedImage.TYPE_INT_RGB);
		// step2.获得画笔
		Graphics g = image.getGraphics();
		// step3.给笔设置颜色
		g.setColor(new Color(255, 255, 255));
		// step4.给画布设置背景颜色
		g.fillRect(0, 0, 80, 30);
		// step5.重新给笔设置颜色
		Random r = new Random();

		// step6.生成number(验证码)
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 5; i++) {
			String str = getNumber(1);
			sb.append(str);
			// 获得一个随机的字符大小
			int h = (int) (30 * 0.3 + 30 * 0.7 * r.nextDouble());
			// 设置字体
			g.setFont(new Font(null, Font.ITALIC | Font.BOLD, h));
			g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			// 将验证码添加到图片里面
			g.drawString(str, 80 / 5 * i, h);
		}
		String number = sb.toString();
		System.out.println("number:" + number);

		// step7.将验证码绑订到session对象上
		HttpSession session = request.getSession();
		session.setAttribute("number", number);

		// step8.加一些干扰线
		for (int i = 0; i < 8; i++) {
			g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			g.drawLine(r.nextInt(80), r.nextInt(30), r.nextInt(80), r.nextInt(30));
		}

		/*
		 * 将图片压缩，发送给浏览器。
		 */
		// step1.设置MIMI类型(告诉浏览器，
		// 服务器返回的数据类型是什么，这儿，
		// 返回的是一张jpeg格式的图片)。
		response.setContentType("image/jpeg");
		// step2.获得字节输出流。
		OutputStream output = response.getOutputStream();
		// step3.压缩图片并输出
		javax.imageio.ImageIO.write(image, "jpeg", output);

	}

	/*
	 * 生成一个长度固定(size个字符),并且随机从 A~Z,0~9中选取的字符组成的验证码
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
