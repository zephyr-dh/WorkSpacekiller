package io.oacy.education.newbie.springbootnewbie.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 案例演示
 */
@RestController
public class FirstController {
    @GetMapping("/hw")
    public String helloWorld(){
        return "Hello World!";
    }

    @GetMapping("/h")
    public String hello(){
        return "Hello!";
    }
}
