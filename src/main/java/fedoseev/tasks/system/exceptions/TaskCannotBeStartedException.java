package fedoseev.tasks.system.exceptions;

public class TaskCannotBeStartedException extends RuntimeException {
    public TaskCannotBeStartedException(String message) {
        super(message);
    }
}
