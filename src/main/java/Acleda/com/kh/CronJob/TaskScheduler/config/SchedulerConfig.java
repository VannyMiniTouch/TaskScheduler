package Acleda.com.kh.CronJob.TaskScheduler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

     @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10); 
        taskScheduler.setThreadNamePrefix("DynamicScheduler-");
        taskScheduler.initialize();
        return taskScheduler;
    }
}
