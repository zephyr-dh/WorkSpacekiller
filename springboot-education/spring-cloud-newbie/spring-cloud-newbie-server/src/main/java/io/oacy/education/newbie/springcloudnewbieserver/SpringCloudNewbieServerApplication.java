package io.oacy.education.newbie.springcloudnewbieserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SpringCloudNewbieServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudNewbieServerApplication.class, args);
    }
}
