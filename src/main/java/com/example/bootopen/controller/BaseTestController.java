package com.example.bootopen.controller;

import com.example.bootopen.common.utils.util.GsonUtils;
import com.example.bootopen.common.utils.web.api.WebBaseReqVo;
import com.example.bootopen.common.utils.web.api.WebBaseRespVo;
import com.example.bootopen.controller.vo.GetBallReqVo;
import com.example.bootopen.controller.vo.GetBallRespVo;
import com.example.bootopen.controller.vo.TestReqVo;
import com.example.bootopen.controller.vo.base.WebCommonReq;
import com.example.bootopen.controller.vo.base.WebCommonResp;
import com.example.bootopen.db.base.mapper.domain.BaseTestBean;
import com.example.bootopen.service.impl.BallHistoryCrawlerManage;
import com.example.bootopen.service.impl.BaseTestServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@EnableAutoConfiguration
public class BaseTestController {

    @Autowired
    private BaseTestServiceImpl baseTestService;


    @RequestMapping("/test")
    private String index() {
        log.info("test start");
        List<BaseTestBean> queryList = baseTestService.queryBaseTestAll();
        return queryList == null ? "hello test" : GsonUtils.toJson(queryList);
    }

    //http://127.0.0.1:8089/batch/testReq
    @RequestMapping(value = "/testReq", method = {RequestMethod.POST})
    public <T extends WebCommonResp> WebBaseRespVo<T> testReq(@RequestBody WebBaseReqVo<TestReqVo> reqVo) {
        log.info("commonMethod start,reqVo={}", reqVo);
        return baseTestService.commonMethod(reqVo);
    }

    @RequestMapping(value = "/commonReq", method = {RequestMethod.GET, RequestMethod.POST})
    public <T extends WebCommonResp> WebBaseRespVo<T> commonReq(@RequestBody WebBaseReqVo<WebCommonReq> reqVo) {
        log.info("commonMethod start,reqVo={}", reqVo);
        return baseTestService.commonMethod(reqVo);
    }


}
