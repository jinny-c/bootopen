package com.example.bootopen.common.utils.redis;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : Jian Jang
 * @version V1.0.0
 * @Project: caps
 * @Package com.lakala.caps.config
 * @Description: redis自动配置
 * @date Date : 2019年12月23日 15:56
 */
//@Configuration
public class RedissonConfiguration {

    @Value("${redis.servers.info:10.177.84.92:6379}")
    private String address;
    @Value("${redis.connection.timeout:3000}")
    private int timeout;
    @Value("${redis.auth.password:ecp@123}")
    private String password;
    @Value("${redis.connection.max:300}")
    private int connMax;
    @Value("${redis.connection.idle.max:200}")
    private int idleMax;
    @Value("${redis.soTimeout:2000}")
    private int soTimeout;
    @Value("${redis.maxAttempts:5}")
    private int maxAttempts;

    //@Configuration
    @ConditionalOnClass({Redisson.class})
    @ConditionalOnExpression("'${spring.redis.mode}'=='single' or '${spring.redis.mode}'=='cluster' or '${spring.redis.mode}'=='sentinel'")
    protected class RedissonSingleClientConfiguration {

        /**
         * 单机模式 redisson 客户端
         */

        @Bean
        @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "single")
        RedissonClient redissonSingle() {
            Config config = new Config();
            String node = address;
            node = node.startsWith("redis://") ? node : "redis://" + node;
            SingleServerConfig serverConfig = config.useSingleServer()
                    .setAddress(node)
                    .setTimeout(timeout)
                    .setConnectionPoolSize(connMax)
                    .setConnectionMinimumIdleSize(idleMax);
            if (StringUtils.isNotBlank(password)) {
                serverConfig.setPassword(password);
            }
            return Redisson.create(config);
        }


        /**
         * 集群模式的 redisson 客户端
         *
         * @return
         */
        @Bean
        @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "cluster")
        RedissonClient redissonCluster() {

            Config config = new Config();
            String[] nodes = address.split(",");
            List<String> newNodes = new ArrayList(nodes.length);
            Arrays.stream(nodes).forEach((index) -> newNodes.add(
                    index.startsWith("redis://") ? index : "redis://" + index));

            ClusterServersConfig serverConfig = config.useClusterServers()
                    .addNodeAddress(newNodes.toArray(new String[0]))
                    .setIdleConnectionTimeout(soTimeout)
                    .setConnectTimeout(timeout)
                    .setRetryAttempts(maxAttempts)
                    .setMasterConnectionPoolSize(connMax)
                    .setTimeout(timeout);
            if (StringUtils.isNotBlank(password)) {
                serverConfig.setPassword(password);
            }
            return Redisson.create(config);
        }
    }
}
