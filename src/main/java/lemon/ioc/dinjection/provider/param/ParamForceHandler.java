package lemon.ioc.dinjection.provider.param;

import java.lang.annotation.Annotation;
import java.util.Map;

import static net.cassite.style.Style.*;
import static net.cassite.style.aggregation.Aggregation.*;

import net.cassite.style.reflect.MemberSup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.ioc.dinjection.Injector;
import lemon.ioc.dinjection.annotations.Force;
import lemon.ioc.dinjection.binder.Scope;
import lemon.ioc.dinjection.exception.AnnoHandleException;
import lemon.ioc.dinjection.provider.IrrelevantAnnoException;
import lemon.ioc.dinjection.provider.ParamHandler;
import lemon.ioc.dinjection.provider.ParamHandlerChain;

/**
 * Handler for Force annotation. <br>
 * forces a value to be what the string represents.
 *
 * @author lemon
 * @see Force
 */
public class ParamForceHandler implements ParamHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParamForceHandler.class);

    @Override
    public boolean canHandle(Annotation[] annotations) {
        for (Annotation ann : annotations) {
            if (ann.annotationType() == Force.class) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object handle(Scope scope, MemberSup<?> caller, Class<?> cls, Class<?> expectedClass, Annotation[] toHandle, ParamHandlerChain chain) throws AnnoHandleException {
        LOGGER.debug("Entered ParamForceHandler with args:\n\tcaller:\t{}\n\tcls:\t{}\n\ttoHandle:\t{}\n\tchain:\t{}", caller, cls, toHandle, chain);
        try {
            return chain.next().handle(scope, caller, cls, expectedClass, toHandle, chain);
        } catch (IrrelevantAnnoException e) {
            LOGGER.debug("Start handling with ParamForceHandler");

            return If((Force) $(toHandle).findOne(a -> a.annotationType() == Force.class), f -> {
                String value = f.value();
                if (!f.properties().equals("")) {
                    Map<Object, Object> map = scope.get(f.properties());
                    assert map != null;
                    value = map.get(value).toString();
                }
                try {
                    if (cls == int.class || cls == Integer.class) {
                        return Integer.parseInt(value);
                    } else if (cls == boolean.class || cls == Boolean.class) {
                        return Boolean.parseBoolean(value);
                    } else if (cls == char.class || cls == Character.class) {
                        return value.charAt(0);
                    } else if (cls == double.class || cls == Double.class) {
                        return Double.parseDouble(value);
                    } else if (cls == float.class || cls == Float.class) {
                        return Float.parseFloat(value);
                    } else if (cls == byte.class || cls == Byte.class) {
                        return Byte.parseByte(value);
                    } else if (cls == long.class || cls == Long.class) {
                        return Long.parseLong(value);
                    } else if (cls == Short.class || cls == short.class) {
                        return Short.parseShort(value);
                    } else if (cls == String.class) {
                        return value;
                    }
                } catch (Exception e1) {
                    throw new AnnoHandleException("parse failed", e1);
                }
                throw new AnnoHandleException("parse failed");
            }).Else(() -> {
                throw new IrrelevantAnnoException();
            });
        }
    }

}
