package fedoseev.tasks.system.task.exceptions;

public class InvalidTaskIdException  extends RuntimeException {
    public InvalidTaskIdException(String message) {
        super(message);
    }
}
