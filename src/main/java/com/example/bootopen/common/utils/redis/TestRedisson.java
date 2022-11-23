package com.example.bootopen.common.utils.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;

public class TestRedisson {

    @Value("${redis.servers.info:10.177.84.92:6379}")
    private String address;
    @Value("${redis.connection.timeout:3000}")
    private int timeout;
    @Value("${redis.auth.password:ecp@123}")
    private String password;
    @Value("${redis.connection.max:300}")
    private int connMax;
    @Value("${redis.connection.idle.max:200}")
    private int idleMax;
    @Value("${redis.soTimeout:2000}")
    private int soTimeout;
    @Value("${redis.maxAttempts:5}")
    private int maxAttempts;

    private static RedissonClient redissonClient() {
        Config config = new Config();
        config.useClusterServers()
                .setScanInterval(2000)
                .setPassword("ecp@123")
                .addNodeAddress("redis://10.177.84.92:6379", "redis://10.177.84.92:7379")
                .addNodeAddress("redis://10.177.84.92:8379");

        RedissonClient redisson = Redisson.create(config);

        return redisson;
    }

    private static final String REDIS_KEY = "lock_test_redis_key";

    public static void main(String[] args) {
        System.out.println("===========================> start");
        RedissonClient redissonClient = redissonClient();
        //分布式锁
        RLock rLock = RedissonUtils.getRLock(redissonClient, REDIS_KEY);
        //获取锁
        if (rLock.tryLock()) {
            try {
                System.out.println("===========================> 获取锁成功");
            } catch (Exception e) {
                System.out.println("===========================> 获取锁异常");
            } finally {
                //解锁
                RedissonUtils.rLockUnLock(rLock);
            }
        } else {
            System.out.println("===========================> 获取锁失败");
        }
        //获取锁2s后自动释放
        if (RedissonUtils.getRLockTryLock(rLock,2000L)) {
            try {
                System.out.println("===========================> 获取锁成功");
            } catch (Exception e) {
                System.out.println("===========================> 获取锁异常");
            }
        } else {
            System.out.println("===========================> 获取锁失败");
        }
    }
}
