package net.cassite.pure.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.cassite.style.interfaces.RFunc0;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibHandler implements MethodInterceptor, Handler {

    private final Weaver[] weavers;
    private final Generator targetFunc;
    private final Class<?> expectingClass;

    private int currentWeaverCursor;

    CglibHandler(Weaver[] weavers, Generator target, Class<?> expectingClass) {
        this.weavers = weavers;
        this.targetFunc = target;
        this.expectingClass = expectingClass;
    }

    @Override
    public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
        AOPPoint p = new AOPPoint(targetFunc.get(), arg1, arg2);
        currentWeaverCursor = weavers.length - 1;
        for (int i = 0; i < weavers.length; ++i) {
            weavers[i].doBefore(p);
            if (p.doReturn) {
                currentWeaverCursor = i;
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
        for (int i = currentWeaverCursor; i >= 0; --i) {
            weavers[i].doAfter(p);
        }
        return p.returnValue();
    }

    @Override
    public Object proxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(expectingClass);

        Set<Class<?>> interfaces = new HashSet<>();
        Collections.addAll(interfaces, expectingClass.getInterfaces());
        for (Weaver w : weavers) {
            Collections.addAll(interfaces, w.getClass().getInterfaces());
        }

        enhancer.setInterfaces(interfaces.toArray(new Class[interfaces.size()]));
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public void destroy() throws Throwable {
        for (int i = currentWeaverCursor; i >= 0; --i) {
            weavers[i].doDestroy(targetFunc.get());
        }
    }

}
