package icu.xuyijie.secureapi.config;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 徐一杰
 * @date 2024/8/29 14:38
 * @description 线程池配置
 */
public class ThreadPoolConfig {
    private ThreadPoolConfig() {

    }

    public static ThreadPoolTaskExecutor secureThreadPool() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(availableProcessors / 2 + 1);
        executor.setMaxPoolSize(availableProcessors);
        executor.setQueueCapacity(Integer.MAX_VALUE);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("secureThreadPool-");
        executor.setTaskDecorator(new MyTaskDecorator());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(3);
        executor.initialize();
        return executor;
    }
}
