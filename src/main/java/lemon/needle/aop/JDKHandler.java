package lemon.needle.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * InvocationHandler which supports aop
 *
 * @author lemon
 * @since 0.1.1
 */
class JDKHandler implements InvocationHandler, Handler {

    private final Weaver[] weavers;
    private final Generator targetFunc;
    private int currentWeaverIndex;
    private final Class<?> expectingClass;

    JDKHandler(Weaver[] weavers, Generator target, Class<?> expectingClass) {
        this.weavers = weavers;
        this.targetFunc = target;
        this.expectingClass = expectingClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        AOPPoint p = new AOPPoint<>(targetFunc.get(), method, args);
        currentWeaverIndex = weavers.length - 1;
        for (int i = 0; i < weavers.length; ++i) {
            weavers[i].doBefore(p);
            if (p.doReturn) {
                currentWeaverIndex = i;
                break;
            }
        }
        if (!p.doReturn) {
            try {
                if (p.method.getDeclaringClass().isInstance(p.target)) {
                    p.returnValue(p.method.invoke(p.target, p.args));
                } else {
                    for (Weaver w : weavers) {
                        if (p.method.getDeclaringClass().isInstance(w)) {
                            p.returnValue(p.method.invoke(w, p.args));
                            break;
                        }
                    }
                }
            } catch (Throwable t) {
                if (t instanceof InvocationTargetException) {
                    t = ((InvocationTargetException) t).getTargetException();
                }
                p.setThrowable(t);
                for (int i = weavers.length - 1; i >= 0; --i) {
                    weavers[i].doException(p);
                    if (p.exception() == null)
                        break;
                }
                return p.returnValue();
            }
        }
        for (int i = currentWeaverIndex; i >= 0; --i) {
            weavers[i].doAfter(p);
        }
        return p.returnValue();
    }

    @Override
    public Object proxy() {
        Set<Class<?>> interfaces = new HashSet<>();
        Collections.addAll(interfaces, expectingClass.getInterfaces());
        for (Weaver w : weavers) {
            Collections.addAll(interfaces, w.getClass().getInterfaces());
        }
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces.toArray(new Class[interfaces.size()]), this);
    }

    @Override
    public void destroy() throws Throwable {
        for (int i = currentWeaverIndex; i >= 0; --i) {
            weavers[i].doDestroy(targetFunc.get());
        }
    }

}
