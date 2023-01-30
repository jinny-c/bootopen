package com.example.bootopen.controller.vo;

import com.example.bootopen.controller.vo.base.WebCommonReq;
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
public class TestReqVo extends WebCommonReq {
    private String userName;
    private String reqType;
}
