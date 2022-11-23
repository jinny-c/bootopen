package com.example.bootopen.common.utils.redis;

import org.redisson.api.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类
 */
public class RedissonUtils {

    /**
     * 设置缓存数据
     *
     * @param redissonClient
     * @param key
     * @param value
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void setBucket(RedissonClient redissonClient, String key, Object value) {
        RBucket bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    /**
     * 设置缓存数据-并设置过期时间
     *
     * @param redissonClient
     * @param key
     * @param value
     * @param ttl            过期时间
     * @param timeUnit       时间单位
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void setBucket(RedissonClient redissonClient, String key, Object value, long ttl, TimeUnit timeUnit) {
        RBucket bucket = redissonClient.getBucket(key);
        bucket.set(value, ttl, timeUnit);
    }

    /**
     * 获取字符串对象
     *
     * @param redissonClient
     * @param objectName
     * @return
     */
    public static <T> RBucket<T> getRBucket(RedissonClient redissonClient, String objectName) {
        RBucket<T> bucket = redissonClient.getBucket(objectName);
        return bucket;
    }
    public static String getFromRedis(RedissonClient redissonClient, String objectName) {
        RBucket<String> bucket = redissonClient.getBucket(objectName);
        return bucket.get();
    }

    /**
     * 设置Map对象
     *
     * @param redissonClient
     * @param key
     */
    public static void setRMap(RedissonClient redissonClient, String key, Map<String, Object> map) {
        RMap<String, Object> rMap = redissonClient.getMap(key);
        rMap.putAll(map);
    }

    /**
     * 设置Map对象
     *
     * @param redissonClient
     * @param key
     * @param valueMap
     * @param ttl
     * @param timeUnit
     */
    public static void setRMap(RedissonClient redissonClient, String key, Map<String, Object> valueMap, long ttl, TimeUnit timeUnit) {
        RMap<String, Object> rMap = redissonClient.getMap(key);
        rMap.putAll(valueMap);
        rMap.expire(ttl, timeUnit);
    }

    /**
     * 获取Map对象
     *
     * @param redissonClient
     * @param key
     * @return
     */
    public static <K, V> RMap<K, V> getRMap(RedissonClient redissonClient, String key) {
        RMap<K, V> map = redissonClient.getMap(key);
        return map;
    }

    /**
     * 存储有序集合
     *
     * @param redissonClient
     * @param key
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void setRSortedSet(RedissonClient redissonClient, String key, SortedSet<Object> valueSet) {
        RSortedSet sortedSet = redissonClient.getSortedSet(key);
        sortedSet.addAll(valueSet);
    }

    /**
     * 获取有序集合
     *
     * @param redissonClient
     * @param objectName
     * @return
     */
    public static <V> RSortedSet<V> getRSortedSet(RedissonClient redissonClient, String objectName) {
        RSortedSet<V> sortedSet = redissonClient.getSortedSet(objectName);
        return sortedSet;
    }

    /**
     * 设置集合
     *
     * @param redissonClient
     * @param key
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void setRSet(RedissonClient redissonClient, String key, Set<Object> valueSet) {
        RSet rSet = redissonClient.getSet(key);
        rSet.addAll(valueSet);
    }

    /**
     * 设置集合
     *
     * @param redissonClient
     * @param key
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void setRSet(RedissonClient redissonClient, String key, Set<Object> valueSet, long ttl, TimeUnit timeUnit) {
        RSet rSet = redissonClient.getSet(key);
        rSet.addAll(valueSet);
        rSet.expire(ttl, timeUnit);
    }

    /**
     * 获取集合
     *
     * @param redissonClient
     * @param objectName
     * @return
     */
    public static <V> RSet<V> getRSet(RedissonClient redissonClient, String objectName) {
        RSet<V> rSet = redissonClient.getSet(objectName);
        return rSet;
    }

    /**
     * 设置集合
     *
     * @param redissonClient
     * @param key
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void setRList(RedissonClient redissonClient, String key, List<Object> valueList) {
        RList rList = redissonClient.getList(key);
        rList.addAll(valueList);
    }

    /**
     * 设置集合
     *
     * @param redissonClient
     * @param key
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void setRSet(RedissonClient redissonClient, String key, List<Object> valueList, long ttl, TimeUnit timeUnit) {
        RList rList = redissonClient.getList(key);
        rList.addAll(valueList);
        rList.expire(ttl, timeUnit);
    }

    /**
     * 获取列表
     *
     * @param redissonClient
     * @param objectName
     * @return
     */
    public static <V> RList<V> getRList(RedissonClient redissonClient, String objectName) {
        RList<V> rList = redissonClient.getList(objectName);
        return rList;
    }

    /**
     * 获取队列
     *
     * @param redissonClient
     * @param objectName
     * @return
     */
    public static <V> RQueue<V> getRQueue(RedissonClient redissonClient, String objectName) {
        RQueue<V> rQueue = redissonClient.getQueue(objectName);
        return rQueue;
    }

    /**
     * 获取双端队列
     *
     * @param redissonClient
     * @param objectName
     * @return
     */
    public static <V> RDeque<V> getRDeque(RedissonClient redissonClient, String objectName) {
        RDeque<V> rDeque = redissonClient.getDeque(objectName);
        return rDeque;
    }

    /**
     * 获取锁
     *
     * @param redissonClient
     * @param objectName
     * @return
     */
    public static RLock getRLock(RedissonClient redissonClient, String objectName) {
        RLock rLock = redissonClient.getLock(objectName);
        return rLock;
    }

    /**
     * 获取原子数
     *
     * @param redissonClient
     * @param objectName
     * @return
     */
    public static RAtomicLong getRAtomicLong(RedissonClient redissonClient, String objectName) {
        RAtomicLong rAtomicLong = redissonClient.getAtomicLong(objectName);
        return rAtomicLong;
    }

    /**
     * 获取记数锁
     *
     * @param redissonClient
     * @param objectName
     * @return
     */
    public static RCountDownLatch getRCountDownLatch(RedissonClient redissonClient, String objectName) {
        RCountDownLatch rCountDownLatch = redissonClient.getCountDownLatch(objectName);
        return rCountDownLatch;
    }

    /**
     * 获取消息的Topic
     *
     * @param redissonClient
     * @param objectName
     * @return
     */
    public static RTopic getRTopic(RedissonClient redissonClient, String objectName) {
        RTopic rTopic = redissonClient.getTopic(objectName);
        return rTopic;
    }

    /**
     * 可重入 分布式锁
     * 加锁
     * @param rLock
     * @param leaseTime
     * @return
     */
    public static boolean getRLockTryLock(RLock rLock, long leaseTime) {
        try {
            return rLock.tryLock(0L, leaseTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解锁
     * @param rLock
     */
    public static void rLockUnLock(RLock rLock) {
        if(rLock.isLocked()){
            if(rLock.isHeldByCurrentThread()){
                rLock.unlock();
            }
        }
    }
}
