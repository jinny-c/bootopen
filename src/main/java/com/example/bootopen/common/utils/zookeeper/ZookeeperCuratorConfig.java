package com.example.bootopen.common.utils.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

/**
 * @description TODO
 * @auth chaijd
 * @date 2022/7/21
 */
@Slf4j
public class ZookeeperCuratorConfig {

    private static String ZK_URL = "zookeeper://10.177.84.92:2181?backup=10.177.84.92:2182,10.177.84.92:2184";

    private CuratorFramework curator() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_URL, retryPolicy);
        client.start();
        return client;
    }

    private static final String ZK_CONNECT_STRING = "uat.dubbo-zk.lakala.sh.in:2181";

    private static CuratorFramework initZkClient() {
        return CuratorFrameworkFactory.newClient(
                ZK_CONNECT_STRING,
                new ExponentialBackoffRetry(1000, 3)
        );
    }

    private static void checkNodeSize() {
        try (CuratorFramework client = initZkClient()) {
            client.start();
            checkNodeSize("/dubbo", client);
        } catch (Exception e) {
            log.error("checkNodeSize error", e);
        }
    }

    static void checkNodeSize(String path, CuratorFramework client) throws Exception {
        List<String> children = client.getChildren().forPath(path);
        for (String child : children) {
            String fullPath = path + "/" + child;
            byte[] data = client.getData().forPath(fullPath);
            if (data != null && data.length > 1024 * 1024) { // 大于 1MB
                System.out.println("Large node found: " + fullPath + ", size: " + data.length + " bytes");
            }
            checkNodeSize(fullPath, client); // 递归检查
        }
    }

    private static void deleteService() {
        try (CuratorFramework client = initZkClient()) {
            client.start();
            // 删除指定服务
            String servicePath = "/dubbo/metadata";
            servicePath = "/dubbo/org.apache.dubbo.mock.api.MockService";
//            servicePath = "/dubbo/mapping";
//            servicePath = "/dubbo/config";
            client.delete().deletingChildrenIfNeeded().forPath(servicePath);
            System.out.println("服务已删除: " + servicePath);
        } catch (Exception e) {
            log.error("deleteService error", e);
        }
    }

    public static void main(String[] args) {
        checkNodeSize();
    }
}
