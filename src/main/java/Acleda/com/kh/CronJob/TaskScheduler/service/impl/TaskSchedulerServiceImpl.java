package Acleda.com.kh.CronJob.TaskScheduler.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import Acleda.com.kh.CronJob.TaskScheduler.dto.TaskSchedulerRequestDto;
import Acleda.com.kh.CronJob.TaskScheduler.entity.TaskSchedulerEntity;
import Acleda.com.kh.CronJob.TaskScheduler.repository.TaskSchedulerRepository;
import Acleda.com.kh.CronJob.TaskScheduler.service.DynamicSchedulerService;
import Acleda.com.kh.CronJob.TaskScheduler.service.TaskSchedulerService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TaskSchedulerServiceImpl implements TaskSchedulerService {

    private final TaskSchedulerRepository taskSchedulerRepository;
    private final DynamicSchedulerService dynamicSchedulerService;

    public List<TaskSchedulerEntity> listTasks() {
        return taskSchedulerRepository.findAll();
    }

    @Override
    public String saveTask(TaskSchedulerRequestDto taskDto) {
        TaskSchedulerEntity taskSchedulerEntity = new TaskSchedulerEntity();
        taskSchedulerEntity.setTaskName(taskDto.getTaskName());
        taskSchedulerEntity.setCronExpression(taskDto.getCronExpression());
        taskSchedulerEntity.setEnabled(taskDto.getEnabled());
        taskSchedulerEntity.setInputter(taskDto.getInputter());
        taskSchedulerRepository.save(taskSchedulerEntity);
        dynamicSchedulerService.scheduleTask(taskSchedulerEntity);
        return "Task added and scheduled!";
    }

    @Override
    public String updateTask(TaskSchedulerRequestDto requestDto) {
        TaskSchedulerEntity taskSchedulerEntity = taskSchedulerRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskSchedulerEntity.setTaskName(requestDto.getTaskName());
        taskSchedulerEntity.setCronExpression(requestDto.getCronExpression());
        dynamicSchedulerService.rescheduleTask(requestDto.getId(), requestDto.getCronExpression());
        taskSchedulerRepository.save(taskSchedulerEntity);
        return "Task updated and rescheduled!";
    }

    @Override
    public void deleteTaskById(Long id) {
        dynamicSchedulerService.cancelTask(id);
        taskSchedulerRepository.deleteById(id);
    }

}
