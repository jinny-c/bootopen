package com.example.bootopen.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootopen.service.impl.BaseTestServiceImpl;

@Slf4j
@RestController
@EnableAutoConfiguration
public class BaseTestController {

    @Autowired
    BaseTestServiceImpl baseTestService;

    @RequestMapping("/test")
    private String index() {
        log.info("test start");
        baseTestService.queryBaseTestAll();
        return "hello test";
    }
}
