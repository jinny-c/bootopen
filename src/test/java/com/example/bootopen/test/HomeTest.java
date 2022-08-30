package com.example.bootopen.test;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description TODO
 * @auth chaijd
 * @date 2022/8/30
 */
public class HomeTest {
    public static void main(String[] args) {
        //listTest();
        filePathTest();
    }

    public static void listTest(){
        List<String> ss = new ArrayList<>(10);
        ss.add("001");
        System.out.println(ss.size());
        ss.add(0,"112");
        System.out.println(ss.size());
        ss.add("223");
        ss.add(2,"334");
        ss.add(4,"445");
        System.out.println(ss);
        System.exit(1);
        ss.set(4,"556");
        System.out.println(ss);
    }

    final static String SPLIT_REGEX = "\\\\|/";

    @SneakyThrows
    public static void filePathTest() {
        String path = "172.16.40.42:2222/Z2016911000015/C1010411000013/202208";
        path = "172.16.40.42:2222\\Z2016911000015\\C1010411000013\\202208";
        String name = "C1010411000013_Z2016911000015_20220829_0001.zip.sec";
        String separator = File.separator;
        List<String> list = Arrays.asList(path.split(SPLIT_REGEX, -1));
        System.out.println(list);
        String newPath = StringUtils.join(list.subList(1,list.size()), separator);
        System.out.println(newPath);
        File filePath = new File(newPath);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        File file = new File(newPath.concat(File.separator).concat(name));
        if (!file.exists()) {
            file.createNewFile();
        }
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getCanonicalPath());
    }
}
