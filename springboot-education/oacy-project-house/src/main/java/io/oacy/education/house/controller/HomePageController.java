package io.oacy.education.house.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by donghua on 2018/11/5
 */
@Controller
public class HomePageController {
    @GetMapping("/")
    public String index(){
        return "index";
    }
}
