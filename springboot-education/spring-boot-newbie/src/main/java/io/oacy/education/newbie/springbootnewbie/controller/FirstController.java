package io.oacy.education.newbie.springbootnewbie.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {
    @GetMapping("/hw")
    public String helloWorld(){
        return "Hello World!";
    }
}
