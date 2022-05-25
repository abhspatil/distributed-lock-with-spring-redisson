package com.abhspatil.distributedlock.lock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LockConfig {

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useClusterServers()
                .addNodeAddress("redis://redis-cache-stage.xxxxxx.clustercfg.aps1.cache.amazonaws.com:6379");

        return Redisson.create(config);
    }
}
