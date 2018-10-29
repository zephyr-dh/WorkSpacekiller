package io.oacy.education.newbie.springcloudnewbieuserapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringCloudNewbieUserApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudNewbieUserApiApplication.class, args);
    }
}
