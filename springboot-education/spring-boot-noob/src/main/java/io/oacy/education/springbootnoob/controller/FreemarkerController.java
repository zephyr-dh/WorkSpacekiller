package io.oacy.education.springbootnoob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class FreemarkerController {

    @RequestMapping("/hello")
    public String hello(Model model) {
        Map<String,Object> map=new HashMap<>();
        map.put("msg", "Hello Freemarker");
        model.addAllAttributes(map);
        return "hello";
    }
}
