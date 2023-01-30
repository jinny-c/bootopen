package com.example.bootopen.controller.vo.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @description TODO
 * @auth chaijd
 * @date 2023/1/30
 */
@Setter
@Getter
@ToString
public class WebCommonResp implements Serializable {
    private String userId;
}
