package com.example.bootopen.common.utils.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import java.util.List;

public class ZKNodeScanner {
    private static final String ZK_CONNECT_STRING = "sit.dubbo-zk.lakala.sh.in:2181";
    private static final int BATCH_SIZE = 30;  // 每批处理的节点数
    private static final long MAX_SAFE_SIZE = 800 * 1024;  // 最大安全读取大小(800KB)

    public static void main(String[] args) {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_CONNECT_STRING,
                60000,  // 会话超时时间
                15000,  // 连接超时时间
                new ExponentialBackoffRetry(1000, 3)  // 重试策略
        );
        client.start();

        try {
            // 获取/dubbo下的所有服务接口
            List<String> services = client.getChildren().forPath("/dubbo");
            System.out.println("发现 " + services.size() + " 个服务接口");

            // 逐个处理服务接口
            for (String service : services) {
                processService(client, service);
                Thread.sleep(200);  // 控制处理速度
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }

    private static void processService(CuratorFramework client, String serviceName) {
        String servicePath = "/dubbo/" + serviceName;
        System.out.println("处理服务: " + serviceName);

        try {
            // 获取服务下的子节点类型(providers/consumers/configurators)
            List<String> nodeTypes = client.getChildren().forPath(servicePath);

            for (String nodeType : nodeTypes) {
                String typePath = servicePath + "/" + nodeType;
                System.out.println("  处理 " + nodeType + " 节点");

                // 分页获取子节点
                processNodeType(client, typePath);
            }
        } catch (Exception e) {
            System.err.println("处理服务失败: " + e.getMessage());
        }
    }

    private static void processNodeType(CuratorFramework client, String path) throws Exception {
        List<String> children = client.getChildren().forPath(path);
        System.out.println("  " + path + " 下有 " + children.size() + " 个子节点");

        // 分批处理子节点
        for (int i = 0; i < children.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, children.size());
            List<String> batch = children.subList(i, end);

            for (String child : batch) {
                String childPath = path + "/" + child;
                processNode(client, childPath);
            }

            Thread.sleep(100);  // 每批处理后休眠
        }
    }

    private static void processNode(CuratorFramework client, String path) {
        try {
            // 仅获取节点元数据，不读取完整内容
            org.apache.zookeeper.data.Stat stat = client.checkExists().forPath(path);
            if (stat != null) {
                System.out.println("    节点: " + path + ", 大小: " + stat.getDataLength() + " 字节");

                // 对于小节点，可选读取完整内容
                if (stat.getDataLength() < MAX_SAFE_SIZE) {
                    byte[] data = client.getData().forPath(path);
                    // 处理数据...
                } else {
                    System.out.println("    数据超过安全大小，跳过读取");
                }
            }
        } catch (Exception e) {
            System.err.println("    处理节点失败: " + e.getMessage());
        }
    }
}