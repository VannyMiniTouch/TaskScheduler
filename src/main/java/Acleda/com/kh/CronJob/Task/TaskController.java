package Acleda.com.kh.CronJob.Task;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // @GetMapping
    // public List<ScheduledTask> getAllTasks() {
    //     return taskService.listAllTasks();
    // }

    @PostMapping("/start/{id}")
    public String startTask(@PathVariable Long id) {
        taskService.startTask(id);
        return "Task started!";
    }

    @PostMapping("/stop/{id}")
    public String stopTask(@PathVariable Long id) {
        taskService.stopTask(id);
        return "Task stopped!";
    }

    // @GetMapping("/pending")
    // public List<ScheduledTask> getPendingTasks() {
    //     return taskService.listPendingTasks();
    // }
}
