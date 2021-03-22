package org.redisson.demo;

import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class DemoScheduledTask implements Serializable, Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(DemoScheduledTask.class);

    private transient RedissonClient redissonClient;
    private transient RScheduledExecutorService executorService;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Autowired
    public void setExecutorService(RScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void run() {
        final long delay = Math.abs(new Random().nextLong() % 5000L);
        LOG.info("Start processing...");

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        final long count = redissonClient.getAtomicLong("demo-counter").incrementAndGet();
        LOG.info("Current count is: {}", count);
    }

    @PostConstruct
    public void construct() {
        executorService.scheduleAtFixedRate(this, 1000L, 100L, TimeUnit.MILLISECONDS).getTaskId();
    }

    @PreDestroy
    public void destroy() {
        redissonClient.getAtomicLong("demo-counter").delete();
    }
}