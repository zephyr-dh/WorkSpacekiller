package io.oacy.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.oacy.service.DemoService;

public class ClientMain {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "applicationConsumer.xml" });
		context.start();
		DemoService service = (DemoService) context.getBean("demoService");
		System.out.println(service.sayHello("world"));
		context.close();
	}

}
