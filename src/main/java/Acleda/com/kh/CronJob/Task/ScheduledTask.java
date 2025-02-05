package Acleda.com.kh.CronJob.Task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "scheduled_tasks")
public class ScheduledTask {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String cronExpression; // Stores the cron expression
    private boolean active; // True if the task is running
    private String status; // "PENDING", "RUNNING", "COMPLETED"
    private LocalDateTime lastRunTime;

}

