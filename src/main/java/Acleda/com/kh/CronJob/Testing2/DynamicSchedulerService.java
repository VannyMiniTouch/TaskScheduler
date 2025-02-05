package Acleda.com.kh.CronJob.Testing2;

// import java.util.List;
// import java.util.Map;
// import java.util.concurrent.ConcurrentHashMap;
// import java.util.concurrent.ScheduledFuture;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
// import org.springframework.scheduling.support.CronTrigger;
// import org.springframework.stereotype.Service;

// import lombok.AllArgsConstructor;

// @Service
// @AllArgsConstructor
// public class DynamicSchedulerService {


//     private final ThreadPoolTaskScheduler taskScheduler;
//     private final SchedulerConfigRepository schedulerConfigRepository;

//     private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

//     public void scheduleTask(SchedulerConfigEntity config) {
//         ScheduledFuture<?> future = taskScheduler.schedule(
//                 () -> executeTask(config.getId()),
//                 new CronTrigger(config.getCronExpression())
//         );
//         scheduledTasks.put(config.getId(), future);
//     }

//     public void rescheduleTask(Long taskId, String newCronExpression) {
//         cancelTask(taskId);
//         SchedulerConfigEntity config = schedulerConfigRepository.findById(taskId)
//                 .orElseThrow(() -> new RuntimeException("Task not found"));
//         config.setCronExpression(newCronExpression);
//         scheduleTask(config);
//     }

//     public void cancelTask(Long taskId) {
//         if (scheduledTasks.containsKey(taskId)) {
//             scheduledTasks.get(taskId).cancel(false);
//             scheduledTasks.remove(taskId);
//         }
//     }

//     public void refreshAllTasks() {
//         scheduledTasks.keySet().forEach(this::cancelTask);
//         List<SchedulerConfigEntity> configs = schedulerConfigRepository.findByEnabled(true);
//         configs.forEach(this::scheduleTask);
//     }

//     public void skipTask(Long taskId) {
//         if (scheduledTasks.containsKey(taskId)) {
//             scheduledTasks.get(taskId).cancel(false);
//         }
//     }

//     private void executeTask(Long taskId) {
//         System.out.println("Executing task: " + taskId);
//         // Add your task logic here
//     }
// }

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@AllArgsConstructor
public class DynamicSchedulerService {

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    private final SchedulerConfigRepository schedulerConfigRepository;

    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // Fetch all enabled tasks from the database
        List<SchedulerConfigEntity> tasks = schedulerConfigRepository.findByEnabled(true);

        // Schedule each task
        for (SchedulerConfigEntity task : tasks) {
            scheduleTask(task);
        }
    }

    public void scheduleTask(SchedulerConfigEntity config) {
        ScheduledFuture<?> future = taskScheduler.schedule(
                () -> executeTask(config.getId()),
                new CronTrigger(config.getCronExpression())
        );
        scheduledTasks.put(config.getId(), future);
    }

    public void rescheduleTask(Long taskId, String newCronExpression) {
        cancelTask(taskId);
        SchedulerConfigEntity config = schedulerConfigRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        config.setCronExpression(newCronExpression);
        scheduleTask(config);
    }

    public void cancelTask(Long taskId) {
        if (scheduledTasks.containsKey(taskId)) {
            scheduledTasks.get(taskId).cancel(false);
            scheduledTasks.remove(taskId);
        }
    }

    public void refreshAllTasks() {
        scheduledTasks.keySet().forEach(this::cancelTask);
        List<SchedulerConfigEntity> configs = schedulerConfigRepository.findByEnabled(true);
        configs.forEach(this::scheduleTask);
    }

    public void skipTask(Long taskId) {
        if (scheduledTasks.containsKey(taskId)) {
            scheduledTasks.get(taskId).cancel(false);
        }
    }

    private void executeTask(Long taskId) {
        System.out.println("Executing task: " + taskId);
        // Add your task logic here
        
    }
}