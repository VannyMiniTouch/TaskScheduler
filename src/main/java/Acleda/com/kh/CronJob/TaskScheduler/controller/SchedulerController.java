package Acleda.com.kh.CronJob.TaskScheduler.controller;

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

import Acleda.com.kh.CronJob.TaskScheduler.entity.TaskSchedulerEntity;
import Acleda.com.kh.CronJob.TaskScheduler.repository.TaskSchedulerRepository;
import Acleda.com.kh.CronJob.TaskScheduler.service.DynamicSchedulerService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/scheduler")
@AllArgsConstructor
public class SchedulerController {
    
    private final DynamicSchedulerService dynamicSchedulerService;
    private final TaskSchedulerRepository taskSchedulerRepository;

    @GetMapping("/tasks")
    public List<TaskSchedulerEntity> listTasks() {
        return taskSchedulerRepository.findAll();
    }

    @PostMapping("/tasks")
    public ResponseEntity<String> addTask(@RequestBody TaskSchedulerEntity taskSchedulerEntity) {
        taskSchedulerEntity.setEnabled(true);
        taskSchedulerRepository.save(taskSchedulerEntity);
        dynamicSchedulerService.scheduleTask(taskSchedulerEntity);
        return ResponseEntity.ok("Task added and scheduled!");
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<String> updateTask(
            @PathVariable Long id,
            @RequestParam String newCronExpression) {

        TaskSchedulerEntity taskSchedulerEntity = taskSchedulerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        dynamicSchedulerService.rescheduleTask(id, newCronExpression);
        taskSchedulerEntity.setCronExpression(newCronExpression);
        taskSchedulerRepository.save(taskSchedulerEntity);
        return ResponseEntity.ok("Task updated and rescheduled!");
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        dynamicSchedulerService.cancelTask(id);
        taskSchedulerRepository.deleteById(id);
        return ResponseEntity.ok("Task deleted and unscheduled!");
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshTasks() {
        dynamicSchedulerService.refreshAllTasks();
        return ResponseEntity.ok("All tasks refreshed!");
    }

    @PostMapping("/tasks/{id}/skip")
    public ResponseEntity<String> skipTask(@PathVariable Long id) {
        dynamicSchedulerService.skipTask(id);
        return ResponseEntity.ok("Task skipped!");
    }
}
