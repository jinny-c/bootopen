package com.example.bootopen.service;

import com.example.bootopen.db.base.mapper.domain.BaseTestBean;

import java.util.List;

public interface IBaseTestService {
    
    List<BaseTestBean> queryBaseTestAll();
}
