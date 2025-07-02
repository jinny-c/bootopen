package com.example.bootopen.common.utils.zookeeper;

import org.apache.zookeeper.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZooKeeperAsyncExample {
    private static final String ZOOKEEPER_CONNECT_STRING = "sit.dubbo-zk.lakala.sh.in:2181";
    private static final int SESSION_TIMEOUT = 3000;

    public static void main(String[] args) throws Exception {
        // 连接 ZooKeeper
        CountDownLatch connectedSignal = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper(ZOOKEEPER_CONNECT_STRING, SESSION_TIMEOUT, event -> {
            if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                connectedSignal.countDown();
            }
        });
        connectedSignal.await();

        // 异步获取子节点并注册 Watcher
        zk.getChildren("/dubbo", false, new AsyncCallback.ChildrenCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, List<String> children) {
                switch (KeeperException.Code.get(rc)) {
                    case OK:
                        System.out.println("子节点列表: " + children);
                        // 注册 Watcher 监听子节点变化
                        try {
                            zk.getChildren(path, new Watcher() {
                                @Override
                                public void process(WatchedEvent event) {
                                    System.out.println("子节点变更事件: " + event);
                                    // 事件触发后需重新注册 Watcher
                                    try {
                                        zk.getChildren(path, this, null);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case NONODE:
                        System.out.println("节点不存在: " + path);
                        break;
                    default:
                        System.out.println("操作失败: " + KeeperException.create(KeeperException.Code.get(rc), path));
                }
            }
        }, "获取子节点上下文");

        // 保持主线程运行，观察异步回调
        Thread.sleep(60000);
        zk.close();
    }
}