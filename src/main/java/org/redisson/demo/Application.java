package org.redisson.demo;

import org.redisson.api.ExecutorOptions;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.redisson.api.WorkerOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean(destroyMethod = "delete")
    RScheduledExecutorService redissonExecutorService(RedissonClient redissonClient,
            ApplicationContext applicationContext) {
        final WorkerOptions workerOptions = WorkerOptions.defaults()
                .beanFactory(applicationContext.getAutowireCapableBeanFactory())
                .workers(10);

        final RScheduledExecutorService executorService = redissonClient.getExecutorService("demo-executor-service");
        executorService.registerWorkers(workerOptions);
        return executorService;
    }
}