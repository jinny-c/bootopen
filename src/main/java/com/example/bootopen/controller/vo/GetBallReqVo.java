package com.example.bootopen.controller.vo;

import com.example.bootopen.controller.vo.base.WebCommonReq;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @description TODO
 * @auth chaijd
 * @date 2023/1/30
 */
@Getter
@Setter
@ToString
public class GetBallReqVo implements Serializable {
    private String url;
    private String start;
    private String end;
}
