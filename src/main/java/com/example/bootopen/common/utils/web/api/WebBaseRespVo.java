package com.example.bootopen.common.utils.web.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @description value object值对象
 * @auth chaijd
 * @date 2023/1/30
 */
@Getter
@Setter
@ToString
public class WebBaseRespVo<T> implements Serializable {
    /**
     * 响应码
     */
    private String respCode;
    /**
     * 相应描述
     */
    private String respDescription;
    /**
     * 响应时间
     */
    private String respTime;
    /**
     * 响应参数
     */
    private T respData;
}
