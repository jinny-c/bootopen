package com.example.bootopen.common.utils.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;

/**
 * @description TODO
 * @auth chaijd
 * @date 2022/7/21
 *
 *
 *
 */
public class ZookeeperCuratorConfig {

    private static String ZK_URL = "zookeeper://10.177.84.92:2181?backup=10.177.84.92:2182,10.177.84.92:2184";

    private CuratorFramework curator() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_URL, retryPolicy);
        client.start();
        return client;
    }
}
