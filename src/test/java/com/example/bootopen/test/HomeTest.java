package com.example.bootopen.test;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description TODO
 * @auth chaijd
 * @date 2022/8/30
 */
public class HomeTest {
    public static void main(String[] args) {
        //listTest();
        //filePathTest();
        charsetValidate();
    }

    private static void listTest() {
        List<String> ss = new ArrayList<>(10);
        ss.add("001");
        System.out.println(ss.size());
        ss.add(0, "112");
        System.out.println(ss.size());
        ss.add("223");
        ss.add(2, "334");
        ss.add(4, "445");
        System.out.println(ss);
        System.exit(1);
        ss.set(4, "556");
        System.out.println(ss);
    }

    final static String SPLIT_REGEX = "\\\\|/";

    @SneakyThrows
    private static void filePathTest() {
        String path = "172.16.40.42:2222/Z2016911000015/C1010411000013/202208";
        path = "172.16.40.42:2222\\Z2016911000015\\C1010411000013\\202208";
        String name = "C1010411000013_Z2016911000015_20220829_0001.zip.sec";
        String separator = File.separator;
        List<String> list = Arrays.asList(path.split(SPLIT_REGEX, -1));
        System.out.println(list);
        String newPath = StringUtils.join(list.subList(1, list.size()), separator);
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

    private static void charsetValidate() {
        System.out.println(isMessyCode("Ã©Å¸Â©Ã©Â¡ÂºÃ¥Â¹Â³"));
        System.out.println(isMessyCode("你好123ewdfdsbgnk9045"));
        System.out.println(isMessyCode("dssdfsdf*7^%$#32@3jhdhfgoh"));
    }

    /**
     * 是否是特殊字符
     * @param strName
     * @return
     */
    private static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|t*|r*|n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }
    }
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
