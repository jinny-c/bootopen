package com.example.bootopen.controller.vo;

import com.example.bootopen.controller.vo.base.WebCommonResp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @description TODO
 * @auth chaijd
 * @date 2023/1/30
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TestRespVo extends WebCommonResp {
    private String userName;
}
