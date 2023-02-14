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
        respVo.setUrl(reqVo.getUrl());
        respVo.setHistoryCount(String.valueOf(list.size()));
        Integer defNum = convertIntDef(reqVo.getDefaultNumber(), 3);
        if (list.size() > defNum) {
            respVo.setRecently(list.subList(0, defNum));
        } else {
            respVo.setRecently(list);
        }

        frequencyOfListQ(blueList, redList, respVo, reqVo);

        return respVo;
    }

    private Integer convertIntDef(String val, int def) {
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            log.error("convertIntDef Exception:{}", e.getMessage());
        }
        return def;
    }

    /**
     * 统计
     *
     * @param falcons
     * @return
     */
    private Map<String, Long> statisticsFrequency(List<String> falcons) {
        //统计
        Map<String, Long> map = falcons.stream().collect(Collectors.groupingBy(k -> k, Collectors.counting()));
        log.info("size={}", map.size());
        return map;
    }

    /**
     * key排序 并截取
     *
     * @param map
     * @param intercept
     * @return
     */
    private Map<String, Long> sortByKey(Map<String, Long> map, int intercept) {
        //根据key排序
        Map<String, Long> sorceKey = new TreeMap<>(map);
        log.info("sorceKey={}", sorceKey);
        if (intercept == 0 || intercept >= map.size()) {
            return sorceKey;
        }
        Map<String, Long> limitMap = new TreeMap<>();
        sorceKey.entrySet().stream().limit(intercept).forEachOrdered(e -> limitMap.put(e.getKey(), e.getValue()));
        return limitMap;
    }

    /**
     * value排序 并截取
     *
     * @param map
     * @param intercept
     * @return
     */
    private Map<String, Long> sortByValue(Map<String, Long> map, int intercept) {
//        Map<String, Long> sorceMap1 = new LinkedHashMap<>();
//        map.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue()).forEachOrdered(e->sorceMap1.put(e.getKey(),e.getValue()));
//        System.out.println(sorceMap1);
        Map<String, Long> sorceValue = new LinkedHashMap<>();
//        map.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).forEach(e->sorceMap2.put(e.getKey(),e.getValue()));
        //forEachOrdered 通过happensbefore原则保证了它的内存可见性
        //根据value排序
        if (intercept == 0 || intercept >= map.size()) {
            map.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).forEachOrdered(e -> sorceValue.put(e.getKey(), e.getValue()));
            log.info("sorceValue={}", sorceValue);
        } else {
            map.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).limit(intercept).forEachOrdered(e -> sorceValue.put(e.getKey(), e.getValue()));
        }
        return sorceValue;
    }

    @Deprecated
    public void frequencyOfListQ(List<String> blueList, List<String> redList, GetBallRespVo respVo, GetBallReqVo reqVo) {

        boolean show = !StringUtils.equals(reqVo.getListHide(), "1");
        Map<String, Long> blueMap = statisticsFrequency(blueList);
        Map<String, Long> redMap = statisticsFrequency(redList);
        Integer blueIntercept = convertIntDef(reqVo.getBlueSize(), 0);
        Integer redIntercept = convertIntDef(reqVo.getRedSize(), 0);

        if (show) {
            respVo.setBlueOrderByKey(sortByKey(blueMap, blueIntercept).toString());
            respVo.setRedOrderByKey(sortByKey(redMap, redIntercept).toString());
        }
        respVo.setBlueOrderByValue(sortByValue(blueMap, blueIntercept).toString());
        respVo.setRedOrderByValue(sortByValue(redMap, redIntercept).toString());
    }

    @SneakyThrows
    private Document getDocument(String url) {
        Connection mozilla = Jsoup.connect(URLDecoder.decode(url, String.valueOf(StandardCharsets.UTF_8))).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.54 Safari/537.36");
        Document document = mozilla.get();
        //Document document = mozilla.timeout(5000).get();
        return document;
    }

}
