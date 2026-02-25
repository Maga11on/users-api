package com.chakray.users.infrastructure.controller;

import com.chakray.users.application.service.LoginService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) { this.loginService = loginService; }

    @PostMapping
    public String login(@RequestParam String taxId, @RequestParam String password) {
        return loginService.authenticate(taxId, password);
    }
}

