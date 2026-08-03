package fedoseev.tasks.system.task.exceptions;

public class TaskCannotBeStartedException extends RuntimeException {
    public TaskCannotBeStartedException(String message) {
        super(message);
    }
}
