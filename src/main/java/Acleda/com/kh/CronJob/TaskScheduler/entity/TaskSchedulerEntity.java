package Acleda.com.kh.CronJob.TaskScheduler.entity;

import java.security.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TASK_SCHEDULER")
public class TaskSchedulerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TASK_NAME")
    private String taskName;

    @Column(name = "CRON_EXP")
    private String cronExpression;

    @Column(name = "ENABLED")
    private boolean enabled;

    @Column(name = "INPUT_DATE")
    private Timestamp inputDate;

    @Column(name = "INPUTTER")
    private String inputter;

}

