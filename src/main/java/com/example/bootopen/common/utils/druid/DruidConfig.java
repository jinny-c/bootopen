package com.example.bootopen.common.utils.druid;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;


@Configuration  //标识该类被纳入spring容器中实例化并管理
@ServletComponentScan //用于扫描所有的Servlet、filter、listener
public class DruidConfig {
    @Bean
    @ConfigurationProperties(prefix="spring.datasource.druid")
    //加载时读取指定的配置信息,前缀为spring.datasource.druid
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }
}
