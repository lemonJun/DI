package org.osgl.inject;

/**
 * `InjectException` is thrown out when error occurred within
 * dependency injection process
 */
public class InjectException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InjectException(String msg) {
        super(msg);
    }

    public InjectException(String format, Object... args) {
        super(String.format(format, args));
    }

    public static InjectException circularDependency(CharSequence dependencyChain) {
        return new InjectException(String.format("Circular dependency found: %s", dependencyChain));
    }

    public InjectException(Throwable cause) {
        super(cause);
    }

    public InjectException(Throwable cause, String message, Object... args) {
        super(String.format(message, args), cause);
    }
}
