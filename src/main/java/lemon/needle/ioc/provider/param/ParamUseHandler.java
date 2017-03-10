package lemon.needle.ioc.provider.param;

import java.lang.annotation.Annotation;

import net.cassite.style.reflect.MemberSup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.needle.ioc.Injector;
import lemon.needle.ioc.annotations.Use;
import lemon.needle.ioc.binder.Scope;
import lemon.needle.ioc.exception.AnnoHandleException;
import lemon.needle.ioc.provider.IrrelevantAnnoException;
import lemon.needle.ioc.provider.ParamHandler;
import lemon.needle.ioc.provider.ParamHandlerChain;

import static net.cassite.style.Style.*;
import static net.cassite.style.aggregation.Aggregation.*;

/**
 * Handler for Use annotation. <br>
 * returns the instance of the class "use" annotation represents.
 *
 * @author lemon
 * @see Use
 */
public class ParamUseHandler implements ParamHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParamUseHandler.class);

    private final Injector injector;

    public ParamUseHandler(Injector injector) {
        this.injector = injector;
    }

    @Override
    public boolean canHandle(Annotation[] annotations) {
        for (Annotation ann : annotations) {
            if (ann.annotationType() == Use.class) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object handle(Scope scope, MemberSup<?> caller, Class<?> cls, Class<?> expectedClass, Annotation[] toHandle, ParamHandlerChain chain) throws AnnoHandleException {
        LOGGER.debug("Entered ParamUseHandler with args:\n\tcaller:\t{}\n\tcls:\t{}\n\ttoHandle:\t{}\n\tchain:\t{}", caller, cls, toHandle, chain);

        try {
            return chain.next().handle(scope, caller, cls, expectedClass, toHandle, chain);
        } catch (IrrelevantAnnoException e) {
            LOGGER.debug("Start handling with ParamUseHandler");

            return If((Use) $(toHandle).findOne(a -> a.annotationType() == Use.class), use -> {
                Class<?> clazz = use.cls();
                if (clazz != Use.class) {
                    return injector.getInstance(scope, clazz, expectedClass);
                }
                String value = use.value();
                if (!"".equals(value)) {
                    return scope.get(value);
                }
                throw new AnnoHandleException("empty Use annotation");
            }).Else(() -> {
                throw new IrrelevantAnnoException();
            });
        }
    }

}
