package com.example.bootopen.service.impl;

import com.example.bootopen.controller.vo.GetBallReqVo;
import com.example.bootopen.controller.vo.GetBallRespVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description TODO
 * @auth chaijd
 * @date 2023/2/13
 */
@Slf4j
@Component("ballHistory")
public class BallHistoryCrawlerManage {

    public GetBallRespVo crawlerBall(GetBallReqVo reqVo) {
        if (StringUtils.isBlank(reqVo.getUrl())) {
            reqVo.setUrl("https://datachart.500.com/ssq/history/newinc/history.php");
        }
        if (StringUtils.isNoneBlank(reqVo.getStart(), reqVo.getEnd())) {
            //03001代表03年第一期彩票  21036代表21年第36期彩票
            String url = "https://datachart.500.com/ssq/history/newinc/history.php?start=" + reqVo.getStart() + "&end=" + reqVo.getEnd();
            reqVo.setUrl(url);
        }

        Document doc = getDocument(reqVo.getUrl());

        List<String> list = new ArrayList();
        List<String> redList = new ArrayList();
        List<String> blueList = new ArrayList();
        // 获取目标HTML代码
        Elements trs = doc.select("tbody[id=tdata]").select("tr");
        trs.forEach(e -> {
            Elements tds = e.select("td");
            String date = tds.get(0).text();
            String[] redArr = new String[]{tds.get(1).text(), tds.get(2).text(), tds.get(3).text(),
                    tds.get(4).text(), tds.get(5).text(), tds.get(6).text()};
            List<String> reds = Arrays.asList(redArr.clone());
            String red = StringUtils.join(redArr, "-");
            String blue = tds.get(7).text();
            list.add(StringUtils.join(date, "期，红：", red, "，蓝：", blue));
            blueList.add(blue);
            redList.addAll(reds);
        });

        GetBallRespVo respVo = new GetBallRespVo();
        respVo.setHistoryCount(String.valueOf(list.size()));
        if (list.size() > 3) {
            respVo.setRecently(list.subList(0, 3));
        } else {
            respVo.setRecently(list);
        }

        frequencyOfListQ(redList, respVo, false);
        frequencyOfListQ(blueList, respVo, true);
//        System.out.println(list1);
//        System.out.println(list2);
        return respVo;
    }

    public static void frequencyOfListQ(List<String> falcons, GetBallRespVo respVo, boolean isBlue) {
        Map<String, Long> map = new HashMap<>();
        if (CollectionUtils.isEmpty(falcons)) {
            return;
        }
        //统计
        map = falcons.stream().collect(Collectors.groupingBy(k -> k, Collectors.counting()));
        log.info("size={}", map.size());
        //根据key排序
        Map<String, Long> sorceKey = new TreeMap<>(map);
        log.info("sorceKey={}", sorceKey);

//        Map<String, Long> sorceMap1 = new LinkedHashMap<>();
//        map.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue()).forEachOrdered(e->sorceMap1.put(e.getKey(),e.getValue()));
//        System.out.println(sorceMap1);
        Map<String, Long> sorceValue = new LinkedHashMap<>();
//        map.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).forEach(e->sorceMap2.put(e.getKey(),e.getValue()));
        //forEachOrdered 通过happensbefore原则保证了它的内存可见性
        //根据value排序
        map.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).forEachOrdered(e -> sorceValue.put(e.getKey(), e.getValue()));
        log.info("sorceValue={}", sorceValue);
        if (isBlue) {
            respVo.setBlueOrderByKey(sorceKey.toString());
            respVo.setBlueOrderByValue(sorceValue.toString());
        } else {
            respVo.setRedOrderByKey(sorceKey.toString());
            respVo.setRedOrderByValue(sorceValue.toString());
        }
    }

    @SneakyThrows
    private Document getDocument(String url) {
        Connection mozilla = Jsoup.connect(URLDecoder.decode(url, String.valueOf(StandardCharsets.UTF_8))).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.54 Safari/537.36");
        Document document = mozilla.get();
        //Document document = mozilla.timeout(5000).get();
        return document;
    }

}
