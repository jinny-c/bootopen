package com.example.bootopen.controller;

import com.example.bootopen.service.impl.BaseTestServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class HelloController {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    BaseTestServiceImpl baseTestService;

    @RequestMapping("/hello")
    private String index() {
        log.info("hello start");
        baseTestService.queryBaseTestAll();
        return "hello world";
    }
}
