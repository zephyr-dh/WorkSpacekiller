package io.oacy;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServerMain {
	public static void main(String args[]) throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext( new String[] { "applicationProvider.xml" });
		context.start();
		System.out.println("�������ⰴ���˳� ~ ");
		System.in.read();
		context.close();
	}
}
