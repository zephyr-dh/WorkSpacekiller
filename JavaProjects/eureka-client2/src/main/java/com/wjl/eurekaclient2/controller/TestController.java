package com.wjl.eurekaclient2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("test2")
public class TestController {

    @GetMapping("test2")
    public String test() throws InterruptedException {
        System.out.println(LocalDateTime.now().toString()+"test.......start");
        Thread.sleep(100000);
        System.out.println("test.......end");
        return "test";
    }

}
