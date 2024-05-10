package com.example.bootopen.common.utils.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;


@Configuration  //标识该类被纳入spring容器中实例化并管理
@ServletComponentScan //用于扫描所有的Servlet、filter、listener
public class DruidConfig {

    //@ConfigurationProperties(prefix="spring.datasource.druid")
    //加载时读取指定的配置信息,前缀为spring.datasource.druid
    @ConfigurationProperties(prefix="spring.datasource")
    @Bean
    @Primary
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

//    @Bean
//    @Primary
//    public SqlSessionFactory dbSqlSessionFactory(@Qualifier("dbDataSource") DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mybatis/mapper/*.xml"));
//        return bean.getObject();
//    }
//
//    @Bean
//    @Primary
//    public DataSourceTransactionManager dbTransactionManager(@Qualifier("dbDataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    @Bean
//    @Primary
//    public SqlSessionTemplate dbSqlSessionTemplate(@Qualifier("dbSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
}
