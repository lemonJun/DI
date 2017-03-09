package lemon.ioc.dinjection.exception;

/**
 * Thrown when handling an annotation.
 * 
 * @author lemon
 *
 */
public class AnnoHandleException extends DIException {

    /**
     * 
     */
    private static final long serialVersionUID = -8266215180131541540L;

    public AnnoHandleException() {
        super();
    }

    public AnnoHandleException(String msg) {
        super(msg);
    }

    public AnnoHandleException(Throwable t) {
        super(t);
    }

    public AnnoHandleException(String msg, Throwable t) {
        super(msg, t);
    }

}
