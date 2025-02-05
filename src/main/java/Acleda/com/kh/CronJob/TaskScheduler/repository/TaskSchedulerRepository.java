package Acleda.com.kh.CronJob.TaskScheduler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Acleda.com.kh.CronJob.TaskScheduler.entity.TaskSchedulerEntity;

public interface TaskSchedulerRepository extends JpaRepository<TaskSchedulerEntity, Long> {
    List<TaskSchedulerEntity> findByEnabled(boolean enabled);
}

