package Acleda.com.kh.CronJob.TaskScheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import Acleda.com.kh.CronJob.SampleService1.Service1;
import Acleda.com.kh.CronJob.SampleService2.Service2;
import Acleda.com.kh.CronJob.TaskScheduler.entity.TaskSchedulerEntity;
import Acleda.com.kh.CronJob.TaskScheduler.repository.TaskSchedulerRepository;
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

    private final TaskSchedulerRepository taskSchedulerRepository;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    private final Service1 service1;
    private final Service2 service2;


    @PostConstruct
    public void init() {
        List<TaskSchedulerEntity> tasks = taskSchedulerRepository.findByEnabled(true);
        for (TaskSchedulerEntity task : tasks) {
            scheduleTask(task);
        }
    }

    public void scheduleTask(TaskSchedulerEntity taskSchedulerEntity) {
        ScheduledFuture<?> future = taskScheduler.schedule(
                // () -> executeTask(taskSchedulerEntity.getId()),
                () -> executeTask(taskSchedulerEntity.getTaskName()),
                new CronTrigger(taskSchedulerEntity.getCronExpression())
        );
        scheduledTasks.put(taskSchedulerEntity.getId(), future);
    }

    public void rescheduleTask(Long taskId, String newCronExpression) {
        cancelTask(taskId);
        TaskSchedulerEntity taskSchedulerEntity = taskSchedulerRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskSchedulerEntity.setCronExpression(newCronExpression);
        scheduleTask(taskSchedulerEntity);
    }

    public void cancelTask(Long taskId) {
        if (scheduledTasks.containsKey(taskId)) {
            scheduledTasks.get(taskId).cancel(false);
            scheduledTasks.remove(taskId);
        }
    }

    public void refreshAllTasks() {
        scheduledTasks.keySet().forEach(this::cancelTask);
        List<TaskSchedulerEntity> taskSchedulerEntities = taskSchedulerRepository.findByEnabled(true);
        taskSchedulerEntities.forEach(this::scheduleTask);
    }

    public void skipTask(Long taskId) {
        if (scheduledTasks.containsKey(taskId)) {
            scheduledTasks.get(taskId).cancel(false);
        }
    }

    private void executeTask(String taskName) {
        System.out.println("Executing task: " + taskName);
        ///Call service
        switch (taskName) {
            case "task1":
            this.service1.Testing1();
            break;

            case "task2":
                this.service2.run();
                break;
        
            default:
                break;
        }
    }
}