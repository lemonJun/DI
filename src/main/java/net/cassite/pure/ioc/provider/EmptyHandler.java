package net.cassite.pure.ioc.provider;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import net.cassite.pure.ioc.Scope;
import net.cassite.pure.ioc.exception.AnnoHandleException;
import net.cassite.style.reflect.ConstructorSup;
import net.cassite.style.reflect.MemberSup;

/**
 * Handles every kind of annotations. <br>
 * false/null/exception would be returned/thrown in order to end the HandlingChain.
 *
 * @author lemon
 */
public class EmptyHandler implements ParamAnnotationHandler, TypeAnnotationHandler, ConstructorFilter {

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
        throw new IrrelevantAnnotationHandlingException();
    }

    @Override
    public boolean canHandle(Annotation[] annotations) {
        return true;
    }

    @Override
    public Object handle(Scope scope, Class<?> cls, Class<?> expectedClass, HandlerChain chain) throws AnnoHandleException {
        throw new IrrelevantAnnotationHandlingException();
    }

    @Override
    public ConstructorSup<Object> handle(Scope scope, List<ConstructorSup<Object>> cons, ConstructorFilterChain chain) throws AnnoHandleException {
        return null;
    }

}
