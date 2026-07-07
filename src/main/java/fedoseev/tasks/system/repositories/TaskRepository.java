package fedoseev.tasks.system.repositories;

import fedoseev.tasks.system.models.TaskEntity;

import fedoseev.tasks.system.models.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
  Long  countByAssignedUserIdAndStatus(Long assignedUserId, TaskStatus status);
}
