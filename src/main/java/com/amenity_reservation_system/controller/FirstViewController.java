package com.amenity_reservation_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstViewController {

    @GetMapping("/")
    public String firstView(){
        return "first-view";
    }
}
