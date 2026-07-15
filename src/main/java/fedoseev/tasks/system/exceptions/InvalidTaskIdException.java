package fedoseev.tasks.system.exceptions;

public class InvalidTaskIdException  extends RuntimeException {
    public InvalidTaskIdException(String message) {
        super(message);
    }
}
