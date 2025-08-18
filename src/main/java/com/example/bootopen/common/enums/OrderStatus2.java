package com.example.bootopen.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author chaijd
 * @description 生成包含所有被 final 修饰或被 @NonNull 注解的字段的构造函数（“必要参数” 指必须初始化的字段）。
 * @date 2025-08-18
 */
@Getter
@RequiredArgsConstructor // 生成必要参数构造函数
public enum OrderStatus2 {
    PENDING(1, "待支付"),
    PAID(2, "已支付"),
    SHIPPED(3, "已发货"),
    COMPLETED(4, "已完成");

    private final int code;
    private final String desc;

    // 可添加方法（如根据code获取枚举）
    public static OrderStatus2 getByCode(int code) {
        for (OrderStatus2 status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的订单状态码: " + code);
    }
}