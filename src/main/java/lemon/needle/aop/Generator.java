package lemon.needle.aop;

import net.cassite.style.interfaces.RFunc0;

/**
 * it's an object generator, in which the RFunc0[R] apply() method would only be called at most once, it will store the value, and use it for other invocations
 */
public class Generator<R> {
    private final RFunc0<R> func;
    private boolean valueRetrieved = false;
    private R r;

    public Generator(RFunc0<R> func) {
        this.func = func;
    }

    /**
     * get object retrieved from the function
     *
     * @return object retrieved from the function
     * @throws Throwable exception
     */
    public R get() throws Throwable {
        if (!valueRetrieved) {
            synchronized (func) {
                if (!valueRetrieved) {
                    r = func.apply();
                    valueRetrieved = true;
                }
            }
        }
        return r;
    }
}
