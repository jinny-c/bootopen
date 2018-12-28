package com.example.bootopen.db.base.mapper;

import com.example.bootopen.db.base.mapper.domain.BaseTestBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BaseTestMapper {

    List<BaseTestBean> selectAll();
}
