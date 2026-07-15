package fedoseev.tasks.system.models.task.web;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({"message", "detailedMessage", "errorTime"})
public record ErrorResponseDto(
        String message,
        String detailedMessage,
        LocalDateTime errorTime
) {}