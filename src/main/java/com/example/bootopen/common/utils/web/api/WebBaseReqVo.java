package com.example.bootopen.common.utils.web.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description value object值对象
 * @auth chaijd
 * @date 2023/1/30
 */
@Getter
@Setter
@ToString
public class WebBaseReqVo<T> implements Serializable {
    /**
     * 请求ip
     */
    private String requestIp;
    /**
     * 请求系统代码
     */
    @NotBlank(message = "请求系统代码不能为空")
    @Length(max = 16, message = "请求系统代码长度不能大于16位")
    private String reqSysCode;
    /**
     * 请求时间，格式yyyyMMddHHmmss
     */
    //@NotBlank(message = "请求时间不能为空")
    //@Length(min = 14, max = 14, message = "请求时间长度为14位")
    private String reqTime;
    /**
     * 请求参数
     */
    @NotNull(message = "请求参数不能为空")
    @Valid
    private T reqData;
}
