package com.basumatarau.samples.oauth.webclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/client")
public class UserController {

    @GetMapping("/app-user-info")
    public Principal getInfo(Principal principal){
        return principal;
    }
}
