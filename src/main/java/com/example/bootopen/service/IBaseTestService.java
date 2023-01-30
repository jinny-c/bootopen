package com.example.bootopen.service;

import com.example.bootopen.common.utils.web.api.WebBaseReqVo;
import com.example.bootopen.common.utils.web.api.WebBaseRespVo;
import com.example.bootopen.controller.vo.base.WebCommonReq;
import com.example.bootopen.controller.vo.base.WebCommonResp;
import com.example.bootopen.db.base.mapper.domain.BaseTestBean;

import java.util.List;

public interface IBaseTestService {
    
    List<BaseTestBean> queryBaseTestAll();


    <M extends WebCommonReq,N extends WebCommonResp> WebBaseRespVo<N> commonMethod(WebBaseReqVo<M> reqVo);
}
