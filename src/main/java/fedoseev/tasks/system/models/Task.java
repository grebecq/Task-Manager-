package fedoseev.tasks.system.models;

import java.time.LocalDateTime;

public record Task(
        Long id,

        Long creatorId,

        Long assignedUserId,

        TaskStatus status,

        LocalDateTime createDateTime,

        LocalDateTime deadlineDate,

        TaskPriority priority
) {
}

