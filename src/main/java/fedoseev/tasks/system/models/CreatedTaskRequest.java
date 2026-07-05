package fedoseev.tasks.system.models;

import java.time.LocalDateTime;

public record CreatedTaskRequest(

        Long creatorId,

        Long assignedUserId,

        LocalDateTime createDateTime,

        LocalDateTime deadlineDate,

        TaskPriority priority
) {

}
