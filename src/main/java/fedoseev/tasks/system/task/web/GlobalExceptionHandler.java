package fedoseev.tasks.system.task.web;

import fedoseev.tasks.system.task.exceptions.InvalidTaskIdException;
import fedoseev.tasks.system.task.exceptions.TaskAlreadyCompletedException;
import fedoseev.tasks.system.task.exceptions.TaskCannotBeStartedException;
import fedoseev.tasks.system.task.exceptions.TaskNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound
            (Exception e) {
        var errorDto = new ErrorResponseDto(
                "Internal server error",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDto);
    }



    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDto> handleNoSuchElement(NoSuchElementException e) {
        var errorDto = new ErrorResponseDto(
                "handle NoSuchElementException",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFound (EntityNotFoundException e) {

        var errorDto = new ErrorResponseDto(
                "handle EntityNotFoundException",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDto);

    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalStateException(IllegalStateException e) {

        var errorDto = new ErrorResponseDto(
                "handle IllegalStateException",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDto);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        var errorDto = new ErrorResponseDto(
                "handle MethodArgumentNotValidException",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDto);

    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleTaskNotFound(TaskNotFoundException  e) {

        var errorDto = new ErrorResponseDto(
                "Task not found",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDto);

    }

    @ExceptionHandler(InvalidTaskIdException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidTaskId(
            InvalidTaskIdException e
    ){

        var errorDto = new ErrorResponseDto(
                "Invalid task id",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler(TaskAlreadyCompletedException.class)
    public ResponseEntity<ErrorResponseDto> handleAlreadyCompletedException(
            TaskAlreadyCompletedException e
    ){

        var errorDto = new ErrorResponseDto(
                "Invalid task id",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorDto);
    }

    @ExceptionHandler(TaskCannotBeStartedException.class)
    public ResponseEntity<ErrorResponseDto> handleTaskCannotBeStartedException(
            TaskCannotBeStartedException e
    ){

        var errorDto = new ErrorResponseDto(
                "Invalid task id",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorDto);
    }




}
