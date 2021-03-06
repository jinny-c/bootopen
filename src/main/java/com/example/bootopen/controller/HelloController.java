package com.example.bootopen.controller;

import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class HelloController {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping("/hello")
    private String index() {
        log.info("hello start");
        return "hello world";
    }
}
