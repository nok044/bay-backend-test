package com.nok.baybackendtest.controller;

import com.nok.baybackendtest.entity.UserEntity;
import com.nok.baybackendtest.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @RequestMapping(value = "${application.context-path}/register" , method = RequestMethod.POST)
    public UserEntity register(@RequestParam(value = "username", required = true) String username,
                               @RequestParam(value = "password", required = true) String password,
                               @RequestParam(value = "address", required = true) String address,
                               @RequestParam(value = "phone", required = true) String phone,
                               @RequestParam(value = "salary", required = true) Long salary){
        return this.registrationService.register(username, password, address, phone, salary);
    }
}
