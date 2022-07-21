package com.example.bootopen.common.utils.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @description TODO
 * @auth chaijd
 * @date 2022/7/21
 * <p>
 * <p>
 * https://blog.csdn.net/qq_40837310/article/details/107122967?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0-107122967-blog-108869185.pc_relevant_multi_platform_whitelistv3&spm=1001.2101.3001.4242.1&utm_relevant_index=3
 * <p>
 * https://blog.51cto.com/u_15368284/5115965?b=totalstatistic
 * <p>
 * <p>
 * InterProcessMutex：分布式可重入排它锁
 * InterProcessSemaphoreMutex：分布式排它锁
 * InterProcessReadWriteLock：分布式读写锁
 */
public class TestZookeeper {
    private static CuratorFramework getZkClient() {
        String zkServerAddress = "10.177.84.92:2181";
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3, 5000);
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                .connectString(zkServerAddress)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        return zkClient;
    }

    public static void main(String[] args) {
        CuratorFramework zkClient = getZkClient();
        String lockPath = "/lock/test";
        InterProcessMutex lock = new InterProcessMutex(zkClient, lockPath);
        //模拟50个线程抢锁
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> run(finalI, lock)).start();
        }
    }

    /**
     *
     *  lock.acquire();
     * 多个个客户端来竞争锁，各自创建自己的节点，按照顺序创建，谁排在第一个，谁就成功的获取了锁。
     * 就像排队买东西一样，谁排在第一个，谁就先买。
     *
     * @param threadFlag
     * @param lock
     */
    private static void run(Integer threadFlag, InterProcessMutex lock) {
        try {
            lock.acquire();
            System.out.printf("==============" + lock.getParticipantNodes());
            System.out.println("第【" + threadFlag + "】 线程获取到了锁");
            //等到1秒后释放锁
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
