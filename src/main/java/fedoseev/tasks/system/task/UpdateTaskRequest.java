package fedoseev.tasks.system.task;



import java.time.LocalDateTime;

public record UpdateTaskRequest(

        Long assignedUserId,

        LocalDateTime deadlineDate,

        TaskPriority priority
) {
}
