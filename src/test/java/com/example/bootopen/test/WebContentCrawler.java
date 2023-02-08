package com.example.bootopen.test;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description TODO
 * @auth chaijd
 * @date 2023/2/8
 */
public class WebContentCrawler {
    public static void main(String[] args) {
        new WebContentCrawler().crawlerTest();
    }

    /**
     * 爬取 网页数据
     */
    @SneakyThrows
    private void crawlerTest() {

        //03001代表03年第一期彩票  21036代表21年第36期彩票
//        Document doc = getDocument("https://datachart.500.com/ssq/history/newinc/history.php?start=21001&end=23013");
        Document doc = getDocument("https://datachart.500.com/ssq/history/newinc/history.php");

        //Element element = doc.body();
        //System.out.println(element.html());

        List<String> list = new ArrayList();
        List<String> list1 = new ArrayList();
        List<String> list2 = new ArrayList();
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
            list2.add(blue);
            list1.addAll(reds);
        });

        System.out.println(list.size());
        frequencyOfListQ(list1);
        frequencyOfListQ(list2);
//        System.out.println(list1);
//        System.out.println(list2);
    }

    public static void frequencyOfListQ(List<String> falcons) {
        Map<String, Long> map = new HashMap<>();
        if (CollectionUtils.isEmpty(falcons)) {
            return;
        }
        map = falcons.stream().collect(Collectors.groupingBy(k -> k, Collectors.counting()));

//        System.out.println(map.getClass());
//        System.out.println(map);
        Map<String, Long> map1 = new TreeMap<>(map);
//        System.out.println(map1.getClass());
        System.out.println(map1.size());
        System.out.println(map1);

//        Map<String, Long> sorceMap1 = new LinkedHashMap<>();
//        map.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue()).forEachOrdered(e->sorceMap1.put(e.getKey(),e.getValue()));
//        System.out.println(sorceMap1);
        Map<String, Long> sorceMap2 = new LinkedHashMap<>();
//        map.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).forEach(e->sorceMap2.put(e.getKey(),e.getValue()));
        //forEachOrdered 通过happensbefore原则保证了它的内存可见性
        map.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).forEachOrdered(e->sorceMap2.put(e.getKey(),e.getValue()));
        System.out.println(sorceMap2);
    }

    /**
     * 适用于 jdk 1.8及以下，统计List集合中每个元素出现的次数
     *
     * @param items
     */
    private static void frequencyOfListAll(List<String> items) {
        Map<String, Integer> map = new HashMap<>();
        if (items == null || items.size() == 0) {
            return;
        }
        for (String k : items) {
            Integer count = map.get(k);
            map.put(k, (count == null) ? 1 : count + 1);
        }
        System.out.println(map);
    }

    @SneakyThrows
    private void crawlerTest2() {
        String url = "https://blog.csdn.net/qq_33666602/article/details/86579733?spm=1001.2101.3001.6650.8&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-8-86579733-blog-127193426.pc_relevant_3mothn_strategy_recovery&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-8-86579733-blog-127193426.pc_relevant_3mothn_strategy_recovery&utm_relevant_index=15";
        //
        Connection mozilla = Jsoup.connect(URLDecoder.decode(url, String.valueOf(StandardCharsets.UTF_8))).userAgent("Mozilla");
        Document document = mozilla.get();

        Element element = document.body();
        System.out.println(element.text());

    }

    @SneakyThrows
    private void crawlerTest1() {
        String url = "https://zhuanlan.zhihu.com/p/34206711";
        //
        Connection mozilla = Jsoup.connect(URLDecoder.decode(url, String.valueOf(StandardCharsets.UTF_8))).userAgent("Mozilla");
        Document document = mozilla.get();

        Element element = document.body();

        System.out.printf(element.text());

        Element j_goodsList = element.getElementById("j_goodsList");

        Elements els = j_goodsList.getElementsByClass("gl-item");
        List<Content> list = new ArrayList<>();
        for (Element el : els) {
            String price = el.getElementsByClass("p-price").eq(0).text();
            String name = el.getElementsByClass("p-name").eq(0).text();
            String img = el.getElementsByClass("img").eq(0).text();
            list.add(new Content(name, img, price));
        }
        System.out.println(list);
    }

    @SneakyThrows
    public static Document getDocument(String url) {
        Connection mozilla = Jsoup.connect(URLDecoder.decode(url, String.valueOf(StandardCharsets.UTF_8))).userAgent("Mozilla");
        Document document = mozilla.get();
        //Document document = mozilla.timeout(5000).get();
        return document;
    }

    @ToString
    @Getter
    class Content {
        private String name;
        private String img;
        private String price;

        public Content(String name, String img, String price) {
            this.name = name;
            this.img = img;
            this.price = price;
        }
    }
}
