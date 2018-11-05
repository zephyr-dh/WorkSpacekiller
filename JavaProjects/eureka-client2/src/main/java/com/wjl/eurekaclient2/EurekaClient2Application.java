package com.wjl.eurekaclient2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableEurekaClient
//@EnableHystrix
//@EnableFeignClients("com.wjl.eurekaclient2.feigns")
public class EurekaClient2Application {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClient2Application.class, args);
    }
}
