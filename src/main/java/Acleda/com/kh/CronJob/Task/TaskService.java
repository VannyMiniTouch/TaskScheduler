package Acleda.com.kh.CronJob.Task;


import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Acleda.com.kh.CronJob.Task.service.UserUpdateService;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Service
public class TaskService {

    private final ScheduledTaskRepository taskRepository;
    private final UserUpdateService userUpdateService;
    private final AddressUpdateService addressUpdateService;
    private final ThreadPoolTaskScheduler taskScheduler;

    public TaskService(ScheduledTaskRepository taskRepository,
                       UserUpdateService userUpdateService,
                       AddressUpdateService addressUpdateService) {
        this.taskRepository = taskRepository;
        this.userUpdateService = userUpdateService;
        this.addressUpdateService = addressUpdateService;
        this.taskScheduler = new ThreadPoolTaskScheduler();
        this.taskScheduler.initialize();
    }

    private final java.util.Map<Long, ScheduledFuture<?>> scheduledTasks = new java.util.HashMap<>();

    /**
     * Auto-start active tasks when the application starts.
     */
    @PostConstruct
    public void loadActiveTasksOnStartup() {
        List<ScheduledTask> activeTasks = taskRepository.findByActiveTrue();
        for (ScheduledTask task : activeTasks) {
           // startTask(task.getId());
        }
        System.out.println(activeTasks.size() + " tasks scheduled on application startup.");
    }

    /**
     * Start a scheduled task dynamically based on its cron expression.
     */
    @Transactional
    public void startTask(Long taskId) {
        ScheduledTask task = taskRepository.findById(taskId).orElseThrow();
        
        if (scheduledTasks.containsKey(taskId)) {
            System.out.println("Task already running: " + task.getName());
            return;
        }

        // Schedule the task using the cron expression
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(() -> executeTask(task),
                new CronTrigger(task.getCronExpression()));

        scheduledTasks.put(taskId, scheduledFuture);

        task.setStatus("RUNNING");
        task.setActive(true);
        taskRepository.save(task);
    }

    /**
     * Stop a scheduled task.
     */
    @Transactional
    public void stopTask(Long taskId) {
        if (scheduledTasks.containsKey(taskId)) {
            scheduledTasks.get(taskId).cancel(false);
            scheduledTasks.remove(taskId);
            System.out.println("Task stopped: " + taskId);
        }

        ScheduledTask task = taskRepository.findById(taskId).orElseThrow();
        task.setStatus("STOPPED");
        task.setActive(false);
        taskRepository.save(task);
    }

    /**
     * Execute the actual logic when a task runs.
     */
    private void executeTask(ScheduledTask task) {
        System.out.println("Executing task: " + task.getName());

        // Call services based on task name
        if (task.getName().equalsIgnoreCase("Update Users")) {
            userUpdateService.updateUsers();
        } else if (task.getName().equalsIgnoreCase("Update Addresses")) {
            addressUpdateService.updateAddresses();
        } else {
            System.out.println("No matching service found for task: " + task.getName());
        }

        task.setLastRunTime(java.time.LocalDateTime.now());
        task.setStatus("COMPLETED");
        taskRepository.save(task);
    }
}
