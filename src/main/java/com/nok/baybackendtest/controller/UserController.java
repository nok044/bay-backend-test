package com.nok.baybackendtest.controller;

import com.nok.baybackendtest.entity.UserEntity;
import com.nok.baybackendtest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "${application.context-path}/currentUser" , method = RequestMethod.GET)
    public UserEntity currentUser(){
        return this.userService.currentUser();
    }
}
