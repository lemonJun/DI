package org.osgl.inject;

/**
 * Could be used when programmer think it is not logic to reach somewhere. 
 * For example, the default branch of a switch case on an enum value
 */
public class UnexpectedException extends RuntimeException {

    public UnexpectedException() {
        super();
    }

    public UnexpectedException(String message) {
        super(message);
    }

    public UnexpectedException(String message, Object... args) {
        super(String.format(message, args));
    }
    
    public UnexpectedException(Throwable cause) {
        super(cause);
    }

    public UnexpectedException(Throwable cause, String message, Object... args) {
        super(String.format(message, args), cause);
    }
}
