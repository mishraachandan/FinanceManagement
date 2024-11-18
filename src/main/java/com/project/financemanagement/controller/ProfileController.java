package com.project.financemanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    @Value("${app.message}")
    private  String message;

//    @Autowired
//    public ProfileController(String message) {
//        this.message = message;
//    }

    @GetMapping("/profile")
    public String getProfileMessage() {
        return message;
    }
}
