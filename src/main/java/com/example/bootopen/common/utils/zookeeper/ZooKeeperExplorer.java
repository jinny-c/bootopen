package com.example.bootopen.common.utils.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

public class ZooKeeperExplorer {
    private static final Logger log = LoggerFactory.getLogger(ZooKeeperExplorer.class);
    private static final int MAX_PACKET_SIZE = 5 * 1024 * 1024; // 5MB
    private static final int BATCH_SIZE = 100; // 每批处理的子节点数量

    private final CuratorFramework client;

    public static void main(String[] args) {
        ZooKeeperExplorer explorer = new ZooKeeperExplorer("sit.dubbo-zk.lakala.sh.in:2181");
        explorer.exploreNode("/dubbo");
        explorer.close();
    }

    public ZooKeeperExplorer(String connectionString) {
        // 方式1：通过系统属性设置（推荐）
        System.setProperty("jute.maxbuffer", String.valueOf(MAX_PACKET_SIZE));

        // 创建客户端
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
        client.start();

        // 方式2：通过反射设置（备选，兼容旧版本）
        trySetPacketSizeViaReflection();
    }

    private void trySetPacketSizeViaReflection() {
        try {
            // 获取底层 ZooKeeper 实例
            org.apache.zookeeper.ZooKeeper zk = client.getZookeeperClient().getZooKeeper();

            // 使用反射设置 packetLen 字段
            Field packetLenField = zk.getClass().getDeclaredField("packetLen");
            packetLenField.setAccessible(true);
            packetLenField.set(zk, MAX_PACKET_SIZE);

            log.info("成功通过反射设置 ZooKeeper 包大小为 {} 字节", MAX_PACKET_SIZE);
        } catch (Exception e) {
            log.warn("反射设置包大小失败，将依赖系统属性配置", e);
        }
    }

    public void exploreNode(String path) {
        try {
            // 等待连接建立
            client.blockUntilConnected();

            // 分批获取子节点
            client.getChildren().inBackground(new BackgroundCallback() {
                @Override
                public void processResult(CuratorFramework client, CuratorEvent event) {
                    if (event.getResultCode() == KeeperException.Code.OK.intValue()) {
                        List<String> children = event.getChildren();
                        log.info("Found {} children for path: {}", children.size(), path);

                        // 分批处理子节点
                        for (int i = 0; i < children.size(); i += BATCH_SIZE) {
                            List<String> batch = children.subList(i, Math.min(i + BATCH_SIZE, children.size()));
                            for (String child : batch) {
                                String fullPath = path + "/" + child;
                                try {
                                    exploreNode(fullPath);
                                } catch (Exception e) {
                                    log.error("Failed to explore node: {}", fullPath, e);
                                }
                            }
                        }
                    } else {
                        log.error("Failed to list children for path: {}. Error code: {}",
                                path, event.getResultCode());
                    }
                }
            }).forPath(path);

            // 获取节点数据（谨慎处理大数据）
            client.getData().inBackground(new BackgroundCallback() {
                @Override
                public void processResult(CuratorFramework client, CuratorEvent event) {
                    if (event.getResultCode() == KeeperException.Code.OK.intValue()) {
                        byte[] data = event.getData();
                        if (data != null && data.length > 0) {
                            log.info("Node {} has data size: {} bytes", path, data.length);
                            // 限制打印的数据长度，避免大日志
                            if (data.length < 1024) {
                                log.info("Node data: {}", new String(data));
                            } else {
                                log.info("Node data size too large ({} bytes), skipping content", data.length);
                            }
                        }
                    } else {
                        log.error("Failed to get data for path: {}. Error code: {}",
                                path, event.getResultCode());
                    }
                }
            }).forPath(path);

        } catch (Exception e) {
            log.error("Failed to explore node: {}", path, e);
        }
    }

    public void close() {
        if (client != null) {
            client.close();
        }
    }
}