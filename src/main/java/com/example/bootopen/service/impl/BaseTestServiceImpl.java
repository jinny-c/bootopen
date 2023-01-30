package com.example.bootopen.service.impl;

import com.example.bootopen.common.utils.convert.RespVoConvert;
import com.example.bootopen.common.utils.util.BeanValidatorUtils;
import com.example.bootopen.controller.vo.TestReqVo;
import com.example.bootopen.controller.vo.TestRespVo;
import com.example.bootopen.common.utils.web.api.WebBaseReqVo;
import com.example.bootopen.common.utils.web.api.WebBaseRespVo;
import com.example.bootopen.controller.vo.base.WebCommonReq;
import com.example.bootopen.controller.vo.base.WebCommonResp;
import com.example.bootopen.db.base.mapper.BaseTestMapper;
import com.example.bootopen.db.base.mapper.domain.BaseTestBean;
import com.example.bootopen.service.IBaseTestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jdchai
 */
//@Service(value = "baseTestService")
@Service
@Slf4j
public class BaseTestServiceImpl implements IBaseTestService {

    @Autowired
    private BaseTestMapper baseTestMapper;
    @Autowired
    @SuppressWarnings("unchecked")
    private BeanValidatorUtils beanValidator;

    @Override
    public List<BaseTestBean> queryBaseTestAll() {
        // TODO Auto-generated method stub
        log.info("queryBaseTestAll start");
        List<BaseTestBean> configs = baseTestMapper.selectAll();
        if (null == configs || configs.isEmpty()) {
            log.error("queryMdseRateConfig,configs is null");
            return null;
        }
        log.warn("queryBaseTestAll,configs.size()={}", configs.size());
        return configs;
    }

    @Override
    public <M extends WebCommonReq, N extends WebCommonResp> WebBaseRespVo<N> commonMethod(WebBaseReqVo<M> reqVo) {
        beanValidator.validate(reqVo);
        log.info("commonMethod start,reqVo.getReqData().getClass()={}", reqVo.getReqData().getClass());
        WebBaseRespVo<N> baseRespVo = RespVoConvert.convertSuccessResp();
        if (reqVo.getReqData() instanceof TestReqVo) {
            TestReqVo testReqVo = (TestReqVo)reqVo.getReqData();
            if(StringUtils.equals(testReqVo.getReqType(),"test")){
                TestRespVo respVo = new TestRespVo();
                respVo.setUserId(testReqVo.getUserId());
                respVo.setUserName(testReqVo.getUserName());
                baseRespVo.setRespData((N) respVo);
                return baseRespVo;
            }
        }
        WebCommonResp commonResp = new WebCommonResp();
        commonResp.setUserId(reqVo.getReqData().getUserId());
        baseRespVo.setRespData((N) commonResp);
        return baseRespVo;
    }

}
