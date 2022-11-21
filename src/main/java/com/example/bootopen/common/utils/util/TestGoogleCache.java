package com.example.bootopen.common.utils.util;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * @description TODO
 * @auth chaijd
 * @date 2022/11/21
 */
@Slf4j
public class TestGoogleCache {
    public static void main(String[] args) {
        newPoolDoTask();
    }


    private static void newPoolDoTask() {
        // 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);
//        LoadingCache<String, Optional<String>> cache = new TestGoogleCache().initCache("sync");
        LoadingCache<String, Optional<String>> cache = new TestGoogleCache().initCache("async");
        String[] keys = new String[]{"11", "33"};
        Random random = new Random();


        for (int i = 0; i < 10; i++) {
            final String key = keys[random.nextInt(keys.length)];
            fixedThreadPool.execute(new Runnable() {
                @SneakyThrows
                public void run() {
                    TraceLogUtils.beginTrace();
                    log.info("key={},TraceID={}", key, TraceLogUtils.getTraceId());
                    Thread.sleep(1200);
                    Optional<String> val = cache.get(key);
                    log.info("val={},TraceID={}", val, TraceLogUtils.getTraceId());
                    TraceLogUtils.endTrace();
                }
            });

        }
        fixedThreadPool.shutdown();
    }

    private LoadingCache<String, Optional<String>> initCache(String type) {
        return CacheBuilder.newBuilder()
                //当缓存项上一次更新操作之后的多久会被刷新
                //.refreshAfterWrite(1, TimeUnit.SECONDS)
                .refreshAfterWrite(900, TimeUnit.MILLISECONDS)
                //当缓存项在指定的时间段内没有更新就会被回收
                .expireAfterWrite(1, TimeUnit.MINUTES)
                //.expireAfterWrite(1000, TimeUnit.MILLISECONDS)
                //当缓存项在指定的时间段内没有被读或写就会被回收
                //.expireAfterAccess(dto.getRecycleInterval(), TimeUnit.MINUTES)
                //并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(8)
                //初始容量
                .initialCapacity(128)
                //超过之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(2 * 1024)
                .build(initLoader(type));
    }

    Executor executor = newFixedThreadPool(2);

    private CacheLoader<String, Optional<String>> initLoader(String type) {
        if (StringUtils.equals("sync", type)) {
            return new CacheLoader<String, Optional<String>>() {
                @Override
                public Optional<String> load(String key) throws Exception {
                    log.info("load start,key={}", key);
                    return Optional.fromNullable(loadByKey(key, "sync load"));
                }

                @Override
                public ListenableFuture<Optional<String>> reload(String key, Optional<String> oldValue) throws Exception {
                    log.info("reload start,key={}", key);
                    Preconditions.checkNotNull(key);
                    Preconditions.checkNotNull(oldValue);
                    try {
                        return Futures.immediateFuture(Optional.fromNullable(loadByKey(key, "sync reload")));
                    } catch (Exception e) {
                        log.info("return old value,reload exception", e);
                    }
                    return Futures.immediateFuture(oldValue);
                }
            };
        } else {
            return new CacheLoader<String, Optional<String>>() {
                @Override
                public Optional<String> load(String key) throws Exception {
                    log.info("load start,key={}", key);
                    return Optional.fromNullable(loadByKey(key, "async load"));
                }

                @Override
                public ListenableFuture<Optional<String>> reload(String key, Optional<String> oldValue) throws Exception {
                    log.info("reload start,key={}", key);
                    Preconditions.checkNotNull(key);
                    Preconditions.checkNotNull(oldValue);

                    ListenableFutureTask<Optional<String>> task = ListenableFutureTask.create(() -> {
                        log.info("async reload start,key={}", key);
                        try {
                            return Optional.fromNullable(loadByKey(key, "async reload"));
                        } catch (Exception e) {
                            log.info("return old value,reload exception", e);
                        }
                        return oldValue;
                    });
                    executor.execute(task);
                    return task;
                }
            };
        }
    }

    @SneakyThrows
    private String loadByKey(String key, String msg) {
        log.info("msg={},key={},TraceID={}", msg, key, TraceLogUtils.getTraceId());
        Thread.sleep(800);
        return StringUtils.join(msg, "[", key, "]");
    }
}
