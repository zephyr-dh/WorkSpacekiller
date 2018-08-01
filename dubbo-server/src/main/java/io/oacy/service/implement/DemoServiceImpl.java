package io.oacy.service.implement;

import io.oacy.service.DemoService;

public class DemoServiceImpl implements DemoService {

	public String sayHello(String name) {
		System.out.println("init : " + name);
		return "hello " + name;
	}

}
