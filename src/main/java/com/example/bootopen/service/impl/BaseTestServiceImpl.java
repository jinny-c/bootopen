package com.example.bootopen.service.impl;

import com.example.bootopen.db.base.mapper.BaseTestMapper;
import com.example.bootopen.db.base.mapper.domain.BaseTestBean;
import com.example.bootopen.service.IBaseTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author jdchai
 */
//@Service(value = "baseTestService")
@Service
@Slf4j
public class BaseTestServiceImpl implements IBaseTestService {

    @Autowired
    private BaseTestMapper baseTestMapper;

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

}
