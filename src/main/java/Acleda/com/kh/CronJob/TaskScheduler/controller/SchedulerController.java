package Acleda.com.kh.CronJob.TaskScheduler.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Acleda.com.kh.CronJob.TaskScheduler.dto.TaskSchedulerRequestDto;
import Acleda.com.kh.CronJob.TaskScheduler.entity.TaskSchedulerEntity;
import Acleda.com.kh.CronJob.TaskScheduler.service.DynamicSchedulerService;
import Acleda.com.kh.CronJob.TaskScheduler.service.TaskSchedulerService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/scheduler")
@AllArgsConstructor
public class SchedulerController {

    private final DynamicSchedulerService dynamicSchedulerService;
    private final TaskSchedulerService taskSchedulerService;

    @GetMapping("/tasks/list")
    public List<TaskSchedulerEntity> listTasks() {
        return this.taskSchedulerService.listTasks();
    }

    @PostMapping("/tasks/add")
    public ResponseEntity<String> addTask(@RequestBody TaskSchedulerRequestDto taskSchedulerRequestDto) {
        return ResponseEntity.ok(this.taskSchedulerService.saveTask(taskSchedulerRequestDto));
    }

    @PostMapping("/tasks/update")
    public ResponseEntity<String> updateTask(@RequestBody TaskSchedulerRequestDto taskSchedulerRequestDto) {
        return ResponseEntity.ok(this.taskSchedulerService.updateTask(taskSchedulerRequestDto));
    }

    @PostMapping("/tasks/{id}/delete")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        this.taskSchedulerService.deleteTaskById(id);
        return ResponseEntity.ok("Task deleted and unscheduled!");
    }

    @PostMapping("/tasks/refresh")
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
