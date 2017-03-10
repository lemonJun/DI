package lemon.needle.ioc.provider;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import lemon.needle.ioc.binder.Scope;
import lemon.needle.ioc.exception.AnnoHandleException;
import net.cassite.style.reflect.ConstructorSup;
import net.cassite.style.reflect.MemberSup;

/**
 * Handles every kind of annotations. <br>
 * false/null/exception would be returned/thrown in order to end the HandlingChain.
 *
 * @author lemon
 */
public class EmptyHandler implements ParamHandler, InstanceFactory, ConstructorFilter {

    private static EmptyHandler inst = null;

    private EmptyHandler() {
    }

    public static EmptyHandler getInstance() {
        if (null == inst) {
            synchronized (EmptyHandler.class) {
                if (null == inst) {
                    inst = new EmptyHandler();
                }
            }
        }
        return inst;
    }

    @Override
    public boolean canHandle(Set<Annotation> annotations) {
        return true;
    }

    @Override
    public Object handle(Scope scope, MemberSup<?> caller, Class<?> cls, Class<?> expectedClass, Annotation[] toHandle, ParamHandlerChain chain) throws AnnoHandleException {
        throw new IrrelevantAnnoException();
    }

    @Override
    public boolean canHandle(Annotation[] annotations) {
        return true;
    }

    @Override
    public Object handle(Scope scope, Class<?> cls, Class<?> expectedClass, HandlerChain chain) throws AnnoHandleException {
        throw new IrrelevantAnnoException();
    }

    @Override
    public ConstructorSup<Object> handle(Scope scope, List<ConstructorSup<Object>> cons, ConstructorFilterChain chain) throws AnnoHandleException {
        return null;
    }

}
