package com.example.bootopen.controller;

import com.example.bootopen.common.utils.util.GsonUtils;
import com.example.bootopen.db.base.mapper.domain.BaseTestBean;
import com.example.bootopen.service.impl.BaseTestServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@EnableAutoConfiguration
public class BaseTestController {

    @Autowired
    BaseTestServiceImpl baseTestService;

    @RequestMapping("/test")
    private String index() {
        log.info("test start");
        List<BaseTestBean> queryList = baseTestService.queryBaseTestAll();
        return queryList == null ? "hello test" : GsonUtils.toJson(queryList);
    }
}
