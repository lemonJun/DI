package lemon.ioc.di.provider.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;

import net.cassite.style.reflect.MemberSup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.ioc.di.Scope;
import lemon.ioc.di.exception.AnnoHandleException;
import lemon.ioc.di.provider.IrrelevantAnnotationHandlingException;
import lemon.ioc.di.provider.ParamAnnotationHandler;
import lemon.ioc.di.provider.ParamHandlerChain;

/**
 * Handles primitives and arrays.
 *
 * @author lemon
 */
public class PrimitiveParameterHandler implements ParamAnnotationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrimitiveParameterHandler.class);

    @Override
    public boolean canHandle(Annotation[] annotations) {
        return true;
    }

    @Override
    public Object handle(Scope scope, MemberSup<?> caller, Class<?> cls, Class<?> expectedClass, Annotation[] toHandle, ParamHandlerChain chain) throws AnnoHandleException {
        LOGGER.debug("Entered PrimitiveParameterHandler with args:\n\tcaller:\t{}\n\tcls:\t{}\n\ttoHandle:\t{}\n\tchain:\t{}", caller, cls, toHandle, chain);

        try {
            return chain.next().handle(scope, caller, cls, expectedClass, toHandle, chain);
        } catch (AnnoHandleException e) {
            LOGGER.debug("Start handling with PrimitiveParameterHandler");
            // primitive
            if (cls == boolean.class || cls == Boolean.class) {
                return (false);
            }
            if (cls == int.class || cls == Integer.class) {
                return (0);
            }
            if (cls == short.class || cls == Short.class) {
                return ((short) 0);
            }
            if (cls == long.class || cls == Long.class) {
                return ((long) 0);
            }
            if (cls == byte.class || cls == Byte.class) {
                return ((byte) 0);
            }
            if (cls == double.class || cls == Double.class) {
                return ((double) 0);
            }
            if (cls == float.class || cls == Float.class) {
                return ((float) 0);
            } else if (cls == char.class || cls == Character.class) {
                return ((char) 0);
            } else if (cls.isArray()) {
                if (cls.getComponentType().isPrimitive()) {
                    // not primitive & is array & component is primitive
                    Class<?> clscmp = cls.getComponentType();
                    if (clscmp == boolean.class) {
                        return (new boolean[0]);
                    }
                    if (clscmp == int.class) {
                        return (new int[0]);
                    }
                    if (clscmp == short.class) {
                        return (new short[0]);
                    }
                    if (clscmp == long.class) {
                        return (new long[0]);
                    }
                    if (clscmp == byte.class) {
                        return (new byte[0]);
                    }
                    if (clscmp == double.class) {
                        return (new double[0]);
                    }
                    if (clscmp == float.class) {
                        return (new float[0]);
                    }
                    return (new char[0]);
                }
                // not primitive & is array & component is not primitive
                return Array.newInstance(cls.getComponentType(), 0);
            } else {
                throw new IrrelevantAnnotationHandlingException();
            }
        }
    }

}
