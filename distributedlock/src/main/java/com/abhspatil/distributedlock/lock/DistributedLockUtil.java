package com.abhspatil.distributedlock.lock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DistributedLockUtil {

    @Autowired
    private RedissonClient redissonClient;

    public boolean tryLock(String key) throws InterruptedException {
        RLock lock = redissonClient.getFairLock(key);

        // traditional lock method
        // lock.lock();

        // boolean b = lock.tryLock();

        boolean b = false;
        b = lock.tryLock(10, TimeUnit.SECONDS);


        /**
         *
         * // or acquire lock and automatically unlock it after 10 seconds
         *         lock.lock(10, TimeUnit.SECONDS);
         *
         *         // or wait for lock aquisition up to 100 seconds
         *         // and automatically unlock it after 10 seconds
         *
         *         try {
         *             boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
         *         } catch (InterruptedException e) {
         *             e.printStackTrace();
         *         }
         **/

        return b;
    }

    public boolean tryLock(String key, Integer waitingTime) throws InterruptedException {
        RLock lock = redissonClient.getFairLock(key);
        boolean b = false;
        b = lock.tryLock(waitingTime, TimeUnit.SECONDS);
        return b;
    }

    public void unlock(String key){
        RLock fairLock = redissonClient.getFairLock(key);
        fairLock.unlock();
    }
}
