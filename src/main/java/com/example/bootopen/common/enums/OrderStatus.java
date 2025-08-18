package com.example.bootopen.common.enums;

/**
 * @author chaijd
 * @description TODO
 * @date 2025-08-18
 */
// 用枚举替代常量类
public enum OrderStatus {
    PENDING(1, "待支付"),
    PAID(2, "已支付"),
    SHIPPED(3, "已发货"),
    COMPLETED(4, "已完成");

    private final int code;
    private final String desc;

    OrderStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // 可添加方法（如根据code获取枚举）
    public static OrderStatus getByCode(int code) {
        for (OrderStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的订单状态码: " + code);
    }

    // getter
    public int getCode() { return code; }
    public String getDesc() { return desc; }
}