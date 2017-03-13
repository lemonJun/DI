package lemon.needle.ioc.exception;

import org.osgl.inject.InjectException;

public class NeedleException extends RuntimeException {
    private static final long serialVersionUID = -826621518011541540L;

    public NeedleException(String message) {
        super(message);
    }

    public NeedleException(String message, Throwable cause) {
        super(message, cause);
    }

    public NeedleException(String format, Object... args) {
        super(String.format(format, args));
    }

    public static InjectException circularDependency(CharSequence dependencyChain) {
        return new InjectException(String.format("Circular dependency found: %s", dependencyChain));
    }

    public NeedleException(Throwable cause) {
        super(cause);
    }

    public NeedleException(Throwable cause, String message, Object... args) {
        super(String.format(message, args), cause);
    }
}
