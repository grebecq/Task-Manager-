package fedoseev.tasks.system.task;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record Task(

        Long id,

        @NotNull
        Long creatorId,

        Long assignedUserId,

        TaskStatus status,

        LocalDateTime createDateTime,

        @NotNull
        @Future
        LocalDateTime deadlineDate,

        LocalDateTime doneDateTime,

        @NotNull
        TaskPriority priority
) {
}

