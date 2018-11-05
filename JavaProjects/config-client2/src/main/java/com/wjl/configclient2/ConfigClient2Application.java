package com.wjl.configclient2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ConfigClient2Application {

	public static void main(String[] args) {
		SpringApplication.run(ConfigClient2Application.class, args);
	}
}
