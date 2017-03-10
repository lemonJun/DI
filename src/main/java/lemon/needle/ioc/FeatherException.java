package lemon.needle.ioc;

public class FeatherException extends RuntimeException {
    FeatherException(String message) {
        super(message);
    }

    FeatherException(String message, Throwable cause) {
        super(message, cause);
    }
}
