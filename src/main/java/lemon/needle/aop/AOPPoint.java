package lemon.needle.aop;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Info about proxy
 *
 * @param <T> target object type
 * @author lemon
 * @since 0.1.1
 */
public class AOPPoint<T> {
    /**
     * target to invoke methods on
     */
    public final T target;
    /**
     * arguments to invoke the method, can be changed freely
     */
    public final Object[] args;
    private Object returnValue = null;
    private Throwable exception = null;
    /**
     * method to invoke
     */
    public final Method method;

    boolean doReturn = false;

    AOPPoint(T target, Method method, Object[] args) {
        this.target = target;
        this.method = method;
        this.args = args == null ? new Object[0] : Arrays.copyOf(args, args.length);
    }

    /**
     * set the return value<br>
     * if it's invoked in 'before', the process would directly jump to
     * 'after return'
     *
     * @param r the return value to set
     */
    public void returnValue(Object r) {
        this.doReturn = true;
        this.returnValue = r;
    }

    /**
     * retrieve the return value<br>
     * it will always be null in 'before'
     *
     * @return return value
     */
    public Object returnValue() {
        return returnValue;
    }

    void setThrowable(Throwable t) {
        this.exception = t;
    }

    /**
     * retrieve the exception occurred
     *
     * @return occurred exception
     */
    public Throwable exception() {
        return exception;
    }

    /**
     * remove the recorded exception. which means the exception had been handled
     */
    public void exceptionHandled() {
        setThrowable(null);
    }

    public String toString() {
        return "target: " + target + ", method: " + method + ", args: " + Arrays.toString(args) + ", returnValue: " + returnValue + ", exception: " + exception;
    }
}
