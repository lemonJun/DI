package lemon.needle.ioc.exception;

public class NeedleException extends RuntimeException {
    private static final long serialVersionUID = -826621518011541540L;

    public NeedleException(String message) {
        super(message);
    }

    public NeedleException(String message, Throwable cause) {
        super(message, cause);
    }
}
