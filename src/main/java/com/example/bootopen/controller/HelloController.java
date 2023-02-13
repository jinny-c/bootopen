package com.example.bootopen.controller;

import com.example.bootopen.common.utils.util.GsonUtils;
import com.example.bootopen.controller.vo.GetBallReqVo;
import com.example.bootopen.controller.vo.GetBallRespVo;
import com.example.bootopen.service.impl.BallHistoryCrawlerManage;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(HelloController.class);
    @Resource(name = "ballHistory")
    private BallHistoryCrawlerManage crawlerManage;

    @RequestMapping("/hello")
    private String index() {
        log.info("hello start");
        return "hello world";
    }

    //produces = "application/x-www-form-urlencoded;charset=UTF-8"
    @RequestMapping(value = "/ball/list", method = {RequestMethod.GET, RequestMethod.POST})
    public GetBallRespVo ballList(HttpServletRequest request) {
        GetBallReqVo reqVo = getBallReqVo(request);
        log.info("commonMethod start,reqVo={}", reqVo);
        return crawlerManage.crawlerBall(reqVo);
    }

    private GetBallReqVo getBallReqVo(HttpServletRequest request) {
        Map<String, String> values = getRequestParameters(request);
        Map<String, String> heads = getRequestHeadsInfo(request);
        GetBallReqVo reqVo = GsonUtils.fromJson(GsonUtils.toJson(values), GetBallReqVo.class);
        return reqVo;
    }

    public Map<String, String> getRequestParameters(HttpServletRequest request) {
        Map<String, String> paramesMap = new HashMap<>();
        try {
            Enumeration paramKeys = request.getParameterNames();
            //遍历接收到的元素，获取传递的所有参数信息
            while (paramKeys.hasMoreElements()) {
                String key = (String) paramKeys.nextElement();
                String value = request.getParameter(key);
                paramesMap.put(key, value);
            }
        } catch (Exception e) {
        }
        return paramesMap;
    }

    public Map<String, String> getRequestHeadsInfo(HttpServletRequest request) {
        Map<String, String> headMap = new HashMap<>();
        try {
            Enumeration headerNames = request.getHeaderNames();
            //遍历接收到的元素，获取传递的所有参数信息
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                headMap.put(key, value);
            }
        } catch (Exception e) {
        }
        return headMap;
    }
}
