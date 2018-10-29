package io.oacy.education.newbie.springcloudnewbieuserweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringCloudNewbieUserWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudNewbieUserWebApplication.class, args);
    }
}
