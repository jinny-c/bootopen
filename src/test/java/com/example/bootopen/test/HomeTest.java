package com.example.bootopen.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @description TODO
 * @auth chaijd
 * @date 2022/8/30
 */
public class HomeTest {
    public static void main(String[] args) {
        List<String> ss = new ArrayList<>(10);
        ss.add("001");
        System.out.println(ss.size());
        ss.add(0,"112");
        System.out.println(ss.size());
        ss.add(2,"223");
        ss.add(3,"334");
        ss.add(4,"445");
        System.out.println(ss);
        System.exit(1);
        ss.set(4,"556");
        System.out.println(ss);
    }
}
