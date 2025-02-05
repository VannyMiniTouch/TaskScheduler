package Acleda.com.kh.CronJob.Testing2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scheduler")
public class SchedulerController {

    @Autowired
    private DynamicSchedulerService dynamicSchedulerService;

    @Autowired
    private SchedulerConfigRepository schedulerConfigRepository;

    // List all tasks
    @GetMapping("/tasks")
    public List<SchedulerConfigEntity> listTasks() {
        return schedulerConfigRepository.findAll();
    }

    // Add a new task
    @PostMapping("/tasks")
    public ResponseEntity<String> addTask(@RequestBody SchedulerConfigEntity config) {
        config.setEnabled(true);
        schedulerConfigRepository.save(config);
        dynamicSchedulerService.scheduleTask(config);
        return ResponseEntity.ok("Task added and scheduled!");
    }


    // @PostMapping("/tasks")
    // public ResponseEntity<String> addTask(@RequestBody SchedulerConfigEntity config) {
    //     // Step 1: Schedule the task
    //     dynamicSchedulerService.scheduleTask(config);

    //     // Step 2: Save the task to the database
    //     config.setEnabled(true);
    //     schedulerConfigRepository.save(config);

    //     return ResponseEntity.ok("Task added and scheduled!");
    // }

    // Update a task
    // @PutMapping("/tasks/{id}")
    // public ResponseEntity<String> updateTask(
    //         @PathVariable Long id,
    //         @RequestParam String newCronExpression) {

    //     SchedulerConfigEntity config = schedulerConfigRepository.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Task not found"));

    //     dynamicSchedulerService.rescheduleTask(id, newCronExpression);
    //     config.setCronExpression(newCronExpression);
    //     schedulerConfigRepository.save(config);

    //     return ResponseEntity.ok("Task updated and rescheduled!");
    // }
    @PutMapping("/tasks/{id}")
    public ResponseEntity<String> updateTask(
            @PathVariable Long id,
            @RequestParam String newCronExpression) {

        // Step 1: Fetch the task from the database
        SchedulerConfigEntity config = schedulerConfigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Step 2: Reschedule the task
        dynamicSchedulerService.rescheduleTask(id, newCronExpression);

        // Step 3: Update the database
        config.setCronExpression(newCronExpression);
        schedulerConfigRepository.save(config);

        return ResponseEntity.ok("Task updated and rescheduled!");
    }

    // Delete a task
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        dynamicSchedulerService.cancelTask(id);
        schedulerConfigRepository.deleteById(id);
        return ResponseEntity.ok("Task deleted and unscheduled!");
    }

    // Refresh all tasks
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshTasks() {
        dynamicSchedulerService.refreshAllTasks();
        return ResponseEntity.ok("All tasks refreshed!");
    }

    // Skip a task
    @PostMapping("/tasks/{id}/skip")
    public ResponseEntity<String> skipTask(@PathVariable Long id) {
        dynamicSchedulerService.skipTask(id);
        return ResponseEntity.ok("Task skipped!");
    }
}
