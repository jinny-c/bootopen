package com.example.bootopen.common.enums;

/**
 * @author chaijd
 * @description TODO
 * @date 2025-08-18
 */
// 支付方式枚举（带业务逻辑）
public enum PaymentMethod {
    ALIPAY {
        @Override
        public String getPayUrl() {
            return "https://pay.alipay.com";
        }
    },
    WECHAT_PAY {
        @Override
        public String getPayUrl() {
            return "https://pay.weixin.qq.com";
        }
    };

    // 抽象方法（每个枚举值实现不同逻辑）
    public abstract String getPayUrl();

    // 通用方法
    public static boolean isSupport(String method) {
        for (PaymentMethod pm : values()) {
            if (pm.name().equals(method)) {
                return true;
            }
        }
        return false;
    }
}