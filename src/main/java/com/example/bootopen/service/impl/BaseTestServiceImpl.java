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
    public List<String> queryBaseTestAll() {
        // TODO Auto-generated method stub
        List<BaseTestBean> configs = baseTestMapper.selectAll();
        if (null == configs || configs.isEmpty()) {
            log.info("queryMdseRateConfig,configs is null");
            return null;
        }
        log.info("queryBaseTestAll,configs.size()={}", configs.size());
        return null;
    }

}
