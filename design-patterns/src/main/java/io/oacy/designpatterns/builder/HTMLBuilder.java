package io.oacy.designpatterns.builder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class HTMLBuilder extends Builder {
	private String filename; // �ļ���
	private PrintWriter writer; // ���ڱ�д�ļ���PrintWriter

	public void makeTitle(String title) { // HTML�ļ��ı���
		filename = title + ".html"; // ��������Ϊ�ļ���
		try {
			writer = new PrintWriter(new FileWriter(filename)); // ���� PrintWriter
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.println("<html><head><title>" + title + "</title></head><body>"); // �������
		writer.println("<h1>" + title + "</h1>");
	}

	public void makeString(String str) { // HTML�ļ��е��ַ���
		writer.println("<p>" + str + "</p>"); // ��<p>��ǩ���
	}

	public void makeItems(String[] items) { // HTML�ļ��е���Ŀ
		writer.println("<ul>"); // ��<ul>��<li>���
		for (int i = 0; i < items.length; i++) {
			writer.println("<li>" + items[i] + "</li>");
		}
		writer.println("</ul>");
	}

	public void close() { // ����ĵ�
		writer.println("</body></html>"); // �رձ�ǩ
		writer.close(); // �ر��ļ�
	}

	public String getResult() { // ��д��ɵ��ĵ�
		return filename; // �����ļ���
	}
}
