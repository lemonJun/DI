package lemon.needle.ioc;

public class NeedleException extends RuntimeException {
    NeedleException(String message) {
        super(message);
    }

    NeedleException(String message, Throwable cause) {
        super(message, cause);
    }
}
