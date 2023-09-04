package com.project.yieldcurve.controller;

  // Paket adınız neyse onu buraya ekleyin

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}