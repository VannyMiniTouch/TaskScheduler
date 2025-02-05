package Acleda.com.kh.CronJob.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduledTaskRepository extends JpaRepository<ScheduledTask, Long> {
    List<ScheduledTask> findByActiveTrue(); // Fetch only active tasks
}
