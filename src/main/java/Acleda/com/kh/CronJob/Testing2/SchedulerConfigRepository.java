package Acleda.com.kh.CronJob.Testing2;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulerConfigRepository extends JpaRepository<SchedulerConfigEntity, Long> {
    List<SchedulerConfigEntity> findByEnabled(boolean enabled);
}

