package api.joayo.exception;

public class CannotRemoveException extends RuntimeException {
    public CannotRemoveException() {
        super();
    }

    public CannotRemoveException(String message) {
        super(message);
    }

    public CannotRemoveException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotRemoveException(Throwable cause) {
        super(cause);
    }

}
