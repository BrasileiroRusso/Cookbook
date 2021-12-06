package ru.geekbrains.cookbook.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class FictController {
    @GetMapping
    public String getWelcomePage(){
        return "welcome";
    }
}
