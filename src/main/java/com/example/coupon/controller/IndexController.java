package com.example.coupon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class IndexController {
    @GetMapping("/")
    public String GetIndex() {
        Date date = new Date();
        return date.toString();
    }
}
