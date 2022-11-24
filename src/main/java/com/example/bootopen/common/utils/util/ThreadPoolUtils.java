package com.example.bootopen.common.utils.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @description TODO
 * @auth chaijd
 * @date 2022/11/24
 */
@Slf4j
public class ThreadPoolUtils {

    public static void main(String[] args) {
        newPool();
    }

    private static void newPool() {
        // 一般需要根据任务的类型来配置线程池大小：
        // 如果是CPU密集型任务，就需要尽量压榨CPU，参考值可以设为 NCPU+1
        // 如果是IO密集型任务，参考值可以设置为2*NCPU
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4, 200,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(2), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                //拒绝策略
                log.info("RejectedExecutionHandler");
            }
        });

        //executor.getMaximumPoolSize() + ArrayBlockingQueue的capacity
        //int t = executor.getMaximumPoolSize() + executor.getQueue().remainingCapacity();
        int t = executor.getMaximumPoolSize() + 2;
        //+1个 则超出线程池处理能力
        t += 1;
        for (int i = 1; i <= t; i++) {
            int finalI = i;
            executor.execute(() -> {
                log.info("i={}", finalI);
            });
        }

        executor.shutdown();
    }

    private static void newPool3_1() {
        //延迟定时 线程池
        ScheduledExecutorService scheduledThreadPool = Executors
                .newScheduledThreadPool(5);
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            public void run() {
//				System.out.println("delay 1 seconds, and excute every 3 seconds");
                log.info("delay 1 seconds, and excute every 3 seconds");
            }
        }, 1, 3, TimeUnit.SECONDS);
        // 表示延迟1秒后每3秒执行一次
//		scheduledThreadPool.shutdown();
    }
}
