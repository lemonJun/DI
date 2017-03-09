package lemon.ioc.di.provider.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.ioc.di.Injector;
import lemon.ioc.di.annotations.Inject;
import lemon.ioc.di.annotations.Invoke;
import lemon.ioc.di.binder.Scope;
import lemon.ioc.di.exception.AnnoHandleException;
import lemon.ioc.di.provider.HandlerChain;
import lemon.ioc.di.provider.IrrelevantAnnoException;
import lemon.ioc.di.provider.InstanceFactory;
import net.cassite.style.aggregation.Aggregation;
import net.cassite.style.reflect.FieldSupport;
import net.cassite.style.reflect.MethodSupport;
import net.cassite.style.reflect.Reflect;

/**
 * 
 * Default implementation of TypeAnnotationHandler <br>
 * generate the object then check @wire and @Invoke finally return.<br>
 *
 * @author lemon
 */
public class DefaultFactory implements InstanceFactory {

    private static final Logger logger = LoggerFactory.getLogger(DefaultFactory.class);

    private final Injector injector;

    public DefaultFactory(Injector injector) {
        this.injector = injector;
    }

    @Override
    public boolean canHandle(Annotation[] annotations) {
        return true;
    }

    @Override
    public Object handle(Scope scope, Class<?> cls, Class<?> expectedClass, HandlerChain chain) throws AnnoHandleException {
        logger.debug("default with args: \n\tcls:\t{}\n\tchain:\t{}", cls, chain);
        try {
            return chain.next().handle(scope, cls, expectedClass, chain);
        } catch (IrrelevantAnnoException e) {
            logger.debug("start handling with DefaultTypeHandler");
            if (scope.isBond(cls)) {
                logger.debug("--cls {} is bond, retrieving from IOCController", cls);
                return scope.getBondInstance(cls);
            }

            // primitive
            if (cls == boolean.class || cls == Boolean.class) {
                return false;
            } else if (cls == int.class || cls == Integer.class) {
                return 0;
            } else if (cls == short.class || cls == Short.class) {
                return (short) 0;
            } else if (cls == long.class || cls == Long.class) {
                return (long) 0;
            } else if (cls == byte.class || cls == Byte.class) {
                return (byte) 0;
            } else if (cls == double.class || cls == Double.class) {
                return (double) 0;
            } else if (cls == float.class || cls == Float.class) {
                return (float) 0;
            } else if (cls == char.class || cls == Character.class) {
                return (char) 0;
            } else if (cls.isArray()) {
                if (cls.getComponentType().isPrimitive()) {
                    // not primitive & is array & component
                    // is
                    // primitive
                    Class<?> clscmp = cls.getComponentType();
                    if (clscmp == boolean.class) {
                        return new boolean[0];
                    }
                    if (clscmp == int.class) {
                        return new int[0];
                    }
                    if (clscmp == short.class) {
                        return new short[0];
                    }
                    if (clscmp == long.class) {
                        return new long[0];
                    }
                    if (clscmp == byte.class) {
                        return new byte[0];
                    }
                    if (clscmp == double.class) {
                        return new double[0];
                    }
                    if (clscmp == float.class) {
                        return new float[0];
                    }
                    return new char[0];
                }
                // not primitive & is array & component is not
                // primitive
                return Array.newInstance(cls.getComponentType(), 0);
            }

            // not primitive and not array
            Object constructedObject = injector.constructObject(scope, cls);

            // scan fields and methods for @Wire and @Invoke
            Object[] fields = Reflect.cls(cls).allFields().stream().filter(f -> f.isAnnotationPresent(Inject.class)).toArray();
            Object[] methods = Reflect.cls(cls).allMethods().stream().filter(m -> m.isAnnotationPresent(Inject.class) || m.isAnnotationPresent(Invoke.class)).toArray();
            if (fields.length != 0 || methods.length != 0) {

                Aggregation.$(fields).forEach(fi -> {
                    FieldSupport f = (FieldSupport) fi;
                    if (f.isAnnotationPresent(Inject.class)) {
                        injector.fillField(scope, constructedObject, f);
                    }
                });
                Aggregation.$(methods).forEach(me -> {
                    MethodSupport m = (MethodSupport) me;
                    if (m.isAnnotationPresent(Inject.class)) {
                        injector.invokeSetter(scope, constructedObject, m);
                    } else if (m.isAnnotationPresent(Invoke.class)) {
                        injector.invokeMethod(scope, m, constructedObject);
                    }
                });
            }
            return constructedObject;
        }
    }

}
