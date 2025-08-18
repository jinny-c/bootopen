package com.example.bootopen.common.enums;

/**
 * @author chaijd
 * @description TODO
 * @date 2025-08-18
 */
// 替代传统的Constant类
public enum GlobalConstants {
    // 系统常量
    MAX_RETRY_COUNT(3),
    TIMEOUT_MS(5000),

    // 业务常量
    DEFAULT_PAGE_SIZE(10),
    MAX_PAGE_SIZE(100);

    private final int value;

    GlobalConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
