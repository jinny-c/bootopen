package com.example.bootopen.common.utils.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

@Slf4j
public class SafeZookeeperExplorer {

    public static void main(String[] args) throws Exception {
        // 创建 Curator 客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                "uat.dubbo-zk.lakala.sh.in:2181",
                new ExponentialBackoffRetry(1000, 3)
        );
        client.start();

        try {
            // 开始从根路径 /dubbo 探索
            exploreNode(client, "/dubbo");
        } finally {
            client.close();
        }
    }

    private static void exploreNode(CuratorFramework client, String path) {
        System.out.println("Exploring node: " + path);

        // 异步获取子节点列表以避免阻塞主线程
        try {
            client.getChildren().inBackground(new BackgroundCallback() {
                @Override
                public void processResult(CuratorFramework client, CuratorEvent event) {
                    if (event.getResultCode() == 0) { // 操作成功
                        List<String> children = event.getChildren();
                        for (String child : children) {
                            String fullPath = path + "/" + child;
                            System.out.println("Found child node: " + fullPath);
                            // 递归探索子节点
                            try {
                                exploreNode(client, fullPath);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        // 处理可能的异常，比如 Packet len is out of range!
                        System.err.println("Failed to list children of node: " + path + ". Error code: " + event.getResultCode());
                    }
                }
            }).forPath(path);
        } catch (Exception e) {
            log.error("Failed to list children of node: " + path, e);
        }

        // 尝试获取节点数据（可选）
        try {
            client.getData().inBackground(new BackgroundCallback() {
                @Override
                public void processResult(CuratorFramework client, CuratorEvent event) {
                    if (event.getResultCode() == 0) { // 操作成功
                        byte[] data = event.getData();
                        if (data != null && data.length > 0) {
                            System.out.println("Data length of node " + path + ": " + data.length);
                        }
                    } else {
                        // 可能是 Packet len is out of range! 错误
                        System.err.println("Failed to get data from node: " + path + ". Error code: " + event.getResultCode());
                    }
                }
            }).forPath(path);
        } catch (Exception e) {
            log.error("Failed to get data from node: " + path, e);
        }
    }
}