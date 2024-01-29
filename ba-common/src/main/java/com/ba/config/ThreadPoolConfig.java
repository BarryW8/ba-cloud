package com.ba.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: barryw
 * @Description:
 * @Date: 2024/1/25 17:49
 */
@Component
public class ThreadPoolConfig {

    /**
     * 核心线程数
     */
    @Value("${threadPoolSize}")
    private String threadPoolSize;

    /**
     * 核心线程数
     */
    @Value("${threadSize}")
    private String threadSize;

    /**
     * 核心线程数
     */
    @Value("${medicalProcessThreadSize:5}")
    private String medicalProcessThreadSize;

    /**
     * 任务队列容量
     */
    private static final int QUEUE_CAPACITY = 100;

    /**
     * 等待时间
     */
    private static final int KEEP_ALIVE_TIME = 3;

    @Bean("xxxThreadPool")
    public ThreadPoolExecutor xxxThreadPool() {
        int corePoolSize = Integer.parseInt(threadPoolSize);
        return new ThreadPoolExecutor(
                corePoolSize,
                corePoolSize+10,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(QUEUE_CAPACITY)
        );
    }

}
