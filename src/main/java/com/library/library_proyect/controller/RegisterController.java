package com.library.library_proyect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegisterController {
    @RequestMapping("/registro")
    public String Register() {
        return "register";
    }

}
