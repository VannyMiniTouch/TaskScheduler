package Acleda.com.kh.CronJob.TaskScheduler.service;

import java.util.List;

import Acleda.com.kh.CronJob.TaskScheduler.dto.TaskSchedulerRequestDto;
import Acleda.com.kh.CronJob.TaskScheduler.entity.TaskSchedulerEntity;

public interface TaskSchedulerService {

    List<TaskSchedulerEntity> listTasks();

    String saveTask(TaskSchedulerRequestDto taskSchedulerRequestDto);

    String updateTask(TaskSchedulerRequestDto taskSchedulerRequestDto);

    void deleteTaskById(Long id);
}