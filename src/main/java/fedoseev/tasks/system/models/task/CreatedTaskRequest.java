package fedoseev.tasks.system.models.task;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreatedTaskRequest(

        @NotNull
        Long creatorId,

        Long assignedUserId,

        LocalDateTime createDateTime,

        @NotNull
        @Future
        LocalDateTime deadlineDate,

        @NotNull
        TaskPriority priority
) {

}
