package com.empresa.aplicacion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Metodo que retona la plantilla index
    @GetMapping(value = {"/", "/index"})
    public String index(){
        return "index";
    }

    // Metodo que retona la plantilla home
    @GetMapping("/home")
    public String home(Model model){
        return "home/home";
    }

    // Metodo que retona la plantilla login
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/forbidden")
    public String forbidden(){
        return "error/forbidden";
    }

}