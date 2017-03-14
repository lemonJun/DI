package lemon.needle.exception;

/**
 * thrown by cass.toolbox.ioc
 * 
 * @author lemon
 *
 */
public class DIException extends RuntimeException {

    private static final long serialVersionUID = 4202618483071924377L;

    public DIException() {
        super();
    }

    public DIException(String s) {
        super(s);
    }

    public DIException(Throwable t) {
        super(t);
    }

    public DIException(String s, Throwable t) {
        super(s, t);
    }
}
