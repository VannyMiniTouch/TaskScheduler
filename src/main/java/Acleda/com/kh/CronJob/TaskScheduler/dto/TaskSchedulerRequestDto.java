package Acleda.com.kh.CronJob.TaskScheduler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskSchedulerRequestDto {
    private Long id;
    private String taskName;
    private String cronExpression;
    private Boolean enabled;
    private String inputter;
}
