package com.example.bootopen.db.base.mapper.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class BaseTestBean {
    @Getter
    @Setter
    private String stId;
    @Getter
    @Setter
    private Integer intId;
    @Getter
    @Setter
    private Long lnId;

    @Getter
    @Setter
    private String stVal;

    @Getter
    @Setter
    private Date createTime;

    @Getter
    @Setter
    private Date updateTime;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("BaseTestBean:");
        builder.append("stId:").append(stId)
                .append(",intId:").append(intId)
                .append(",stVal:").append(stVal)
                .append(",createTime:").append(createTime)
                .append(",updateTime:").append(updateTime)
                .append(",lnId:").append(lnId);
        return builder.toString();
    }
}
