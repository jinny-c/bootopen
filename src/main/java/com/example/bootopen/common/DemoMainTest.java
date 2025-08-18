package com.example.bootopen.common;

import com.example.bootopen.common.enums.*;
import lombok.SneakyThrows;

/**
 * @author chaijd
 * @description TODO
 * @date 2025-08-18
 */
public class DemoMainTest {
    public static void main(String[] args) {
        test1();
        test3();
        test2();
    }

    private static void test1() {
        System.out.println(PaymentMethod.ALIPAY);
        System.out.println(PaymentMethod.ALIPAY.name());
        System.out.println(PaymentMethod.ALIPAY.getPayUrl());
        System.out.println(PaymentMethod.isSupport("ALIPAY"));
    }

    @SneakyThrows
    private static void test2() {
        int pageSize = 101;
        if (pageSize > GlobalConstants.MAX_PAGE_SIZE.getValue()) {
            throw new IllegalArgumentException("分页大小超出限制");
        }
    }

    private static void test3() {
        System.out.println(OrderStatus.PAID.getDesc());

        System.out.println(OrderStatus1.COMPLETED.getCode());

        System.out.println(OrderStatus2.SHIPPED);
    }

}
