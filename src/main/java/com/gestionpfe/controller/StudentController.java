package com.gestionpfe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/student")
public class StudentController {

    @GetMapping("/welcome")
    public String getWelcomeMessage() {
        return "Welcome duude!";
    }
}
