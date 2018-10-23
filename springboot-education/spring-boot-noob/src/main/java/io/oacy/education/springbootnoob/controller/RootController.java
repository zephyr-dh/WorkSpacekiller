package io.oacy.education.springbootnoob.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public String root(){
        return "hello, you have accessed root!";
    }

    @GetMapping("/hw")
    public String helloWorld() {
        return "hello world";
    }
}
