package com.library.library_proyect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    @RequestMapping({ "/", "/home" })
    public String home(Model model) {
        model.addAttribute("activePage", "home");
        return "home";
    }
}
