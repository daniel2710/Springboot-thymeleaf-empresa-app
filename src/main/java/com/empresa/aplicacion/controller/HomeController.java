package com.empresa.aplicacion.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

public class HomeController {

    @GetMapping("/forbidden")
    public String forbidden(){
        return "error/forbidden";
    }

}