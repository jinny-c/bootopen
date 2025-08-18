package com.example.bootopen.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chaijd
 * @description 生成包含类中所有非静态字段的构造函数，参数顺序与字段定义顺序一致。
 * @date 2025-08-18
 */
@Getter
@AllArgsConstructor // 生成全参构造函数
public enum OrderStatus1 {
    PENDING(1, "待支付"),
    PAID(2, "已支付"),
    SHIPPED(3, "已发货"),
    COMPLETED(4, "已完成");

    private final int code;
    private final String desc;

    // 可添加方法（如根据code获取枚举）
    public static OrderStatus1 getByCode(int code) {
        for (OrderStatus1 status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的订单状态码: " + code);
    }
}