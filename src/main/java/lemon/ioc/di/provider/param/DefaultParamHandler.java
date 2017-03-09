package lemon.ioc.di.provider.param;

import java.lang.annotation.Annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.ioc.di.Injector;
import lemon.ioc.di.binder.Scope;
import lemon.ioc.di.exception.AnnoHandleException;
import lemon.ioc.di.provider.IrrelevantAnnoException;
import lemon.ioc.di.provider.ParamHandler;
import lemon.ioc.di.provider.ParamHandlerChain;
import net.cassite.style.reflect.MemberSup;

/**
 * Default implementation of ParamAnnotationHandler <br>
 * simply return the corresponding value of given parameter type.
 *
 * @author lemon
 */
public class DefaultParamHandler implements ParamHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultParamHandler.class);

    private final Injector inejctor;

    public DefaultParamHandler(Injector controller) {
        this.inejctor = controller;
    }

    @Override
    public boolean canHandle(Annotation[] annotations) {
        return true;
    }

    @Override
    public Object handle(Scope scope, MemberSup<?> caller, Class<?> cls, Class<?> expectedClass, Annotation[] toHandle, ParamHandlerChain chain) throws AnnoHandleException {
        LOGGER.debug("Entered DefaultParamHandler with args:\n\tcaller:\t{}\n\tcls:\t{}\n\ttoHandle:\t{}\n\tchain:\t{}", caller, cls, toHandle, chain);
        if (scope.isBond(cls)) {
            LOGGER.debug("Start handling with DefaultParamHandler");
            LOGGER.debug("--cls {} is bond, retrieving from IOCController", cls);
            return scope.getBondInstance(cls);
        } else {
            try {
                return chain.next().handle(scope, caller, cls, expectedClass, toHandle, chain);
            } catch (IrrelevantAnnoException e) {
                LOGGER.debug("Start handling with DefaultParamHandler");
                return inejctor.getInstance(scope, cls, expectedClass);
            }
        }
    }

}
