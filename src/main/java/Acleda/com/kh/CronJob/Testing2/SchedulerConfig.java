// package Acleda.com.kh.CronJob.Testing2;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

// @Configuration
// public class SchedulerConfig {

//     @Bean
//     public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
//         ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
//         taskScheduler.setPoolSize(10); // Set the pool size as needed
//         taskScheduler.setThreadNamePrefix("DynamicScheduler-");
//         taskScheduler.initialize();
//         return taskScheduler;
//     }
// }


package Acleda.com.kh.CronJob.Testing2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

     @Bean
    //@Bean(name = "taskScheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10); // Set the pool size as needed
        taskScheduler.setThreadNamePrefix("DynamicScheduler-");
        taskScheduler.initialize();
        return taskScheduler;
    }
}
