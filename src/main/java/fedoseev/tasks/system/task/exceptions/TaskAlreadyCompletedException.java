package fedoseev.tasks.system.task.exceptions;

public class TaskAlreadyCompletedException extends RuntimeException {

    public TaskAlreadyCompletedException(String message) {
        super(message);
    }
}
