package io.oacy.education.newbie.springcloudnewbieuserapi.controller;

import io.oacy.education.newbie.springcloudnewbieuserapi.domain.User;
import io.oacy.education.newbie.springcloudnewbieuserapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/get/{id}")
    public User get(@PathVariable("id") Integer id) {
        return this.userService.getById(id);
    }
}