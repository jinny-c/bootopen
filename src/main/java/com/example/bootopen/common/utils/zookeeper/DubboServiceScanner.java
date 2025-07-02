package com.example.bootopen.common.utils.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DubboServiceScanner {
    private static final int BATCH_SIZE = 5;  // 减小每批处理量
    private static final String ZK_CONNECT_STRING = "uat.dubbo-zk.lakala.sh.in:2181";

    public static void main(String[] args) {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_CONNECT_STRING,
                60000,  // 增加会话超时时间
                15000,
                new ExponentialBackoffRetry(1000, 5)  // 增加重试次数
        );
        client.start();

        try {
            // 分批次获取所有服务
            List<String> allServices = new ArrayList<>();
            int offset = 0;

            while (true) {
                try {
                    List<String> batchServices = getServicesByBatch(client, offset, BATCH_SIZE);
                    if (batchServices.isEmpty()) {
                        break;
                    }

                    allServices.addAll(batchServices);
                    offset += batchServices.size();

                    System.out.println("已获取 " + offset + " 个服务");

                    // 处理当前批次的服务
                    processServiceBatch(client, batchServices);

                    // 每批处理后休眠，减轻压力
                    Thread.sleep(500);

                } catch (Exception e) {
                    System.err.println("获取批次失败，offset=" + offset + ": " + e.getMessage());
                    // 指数退避重试
                    Thread.sleep(1000 * (offset % 5 + 1));
                }
            }

            System.out.println("扫描完成，共发现 " + allServices.size() + " 个服务");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }

    // 安全获取指定批次的服务
    private static List<String> getServicesByBatch(CuratorFramework client, int offset, int batchSize) throws Exception {
        List<String> allChildren = client.getChildren().forPath("/dubbo");
        Collections.sort(allChildren);  // 确保顺序稳定

        int endIndex = Math.min(offset + batchSize, allChildren.size());
        if (offset >= endIndex) {
            return Collections.emptyList();
        }

        return allChildren.subList(offset, endIndex);
    }

    // 处理一批服务
    private static void processServiceBatch(CuratorFramework client, List<String> services) {
        for (String service : services) {
            try {
                // 处理服务的提供者和消费者
                processServiceDetails(client, service);
            } catch (Exception e) {
                System.err.println("处理服务 [" + service + "] 失败: " + e.getMessage());
            }
        }
    }

    // 处理单个服务的详细信息
    private static void processServiceDetails(CuratorFramework client, String serviceName) throws Exception {
        String servicePath = "/dubbo/" + serviceName;

        // 获取提供者（带重试）
        List<String> providers = safeGetChildren(client, servicePath + "/providers");
        System.out.println("服务 [" + serviceName + "] 有 " + providers.size() + " 个提供者");

        // 获取消费者（带重试）
        List<String> consumers = safeGetChildren(client, servicePath + "/consumers");
        System.out.println("服务 [" + serviceName + "] 有 " + consumers.size() + " 个消费者");
    }

    // 安全获取子节点（带重试和异常处理）
    private static List<String> safeGetChildren(CuratorFramework client, String path) throws Exception {
        int retries = 0;
        while (retries < 3) {
            try {
                return client.getChildren().forPath(path);
            } catch (Exception e) {
                retries++;
                System.err.println("获取路径 [" + path + "] 失败，重试 " + retries + "/3: " + e.getMessage());
                Thread.sleep(1000 * retries);  // 指数退避
            }
        }
        return Collections.emptyList();
    }
}