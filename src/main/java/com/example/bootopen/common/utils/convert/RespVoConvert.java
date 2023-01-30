package com.example.bootopen.common.utils.convert;

import com.example.bootopen.common.utils.web.api.WebBaseRespVo;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * @description bean转换
 * @auth chaijd
 * @date 2023/1/30
 */
public class RespVoConvert {

    public static WebBaseRespVo convertSuccessResp() {
        WebBaseRespVo baseRespVo = new WebBaseRespVo();
        baseRespVo.setRespCode("000000");
        baseRespVo.setRespDescription("请求完成");
        baseRespVo.setRespTime(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
        return baseRespVo;
    }

    public static WebBaseRespVo convertResp(String code, String msg) {
        WebBaseRespVo baseRespVo = new WebBaseRespVo();
        baseRespVo.setRespCode(code);
        baseRespVo.setRespDescription(msg);
        baseRespVo.setRespTime(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
        return baseRespVo;
    }
}
