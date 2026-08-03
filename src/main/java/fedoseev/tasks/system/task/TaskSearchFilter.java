package fedoseev.tasks.system.task;

public record TaskSearchFilter(
        Long creatorId,
        Long assignedUserId,
        TaskStatus status,
        TaskPriority priority,
        int pageSize,
        int pageNum
) {
}
