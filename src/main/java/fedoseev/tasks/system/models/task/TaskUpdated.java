package fedoseev.tasks.system.models.task;



import java.time.LocalDateTime;

public record TaskUpdated(

        Long assignedUserId,

        LocalDateTime deadlineDate,

        TaskPriority priority
) {
}
