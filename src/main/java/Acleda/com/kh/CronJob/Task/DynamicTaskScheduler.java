package Acleda.com.kh.CronJob.Task;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class DynamicTaskScheduler {

    private final TaskScheduler taskScheduler;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public DynamicTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        this.taskScheduler = scheduler;
    }

    public void scheduleTask(ScheduledTask task, Runnable runnable) {
        ScheduledFuture<?> future = taskScheduler.schedule(runnable, Instant.now().plusSeconds(5));
        scheduledTasks.put(task.getId(), future);
    }

    public void stopTask(Long taskId) {
        ScheduledFuture<?> future = scheduledTasks.get(taskId);
        if (future != null) {
            future.cancel(false);
            scheduledTasks.remove(taskId);
        }
    }

    public boolean isTaskRunning(Long taskId) {
        return scheduledTasks.containsKey(taskId);
    }
}
