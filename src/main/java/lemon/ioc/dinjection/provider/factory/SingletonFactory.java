package lemon.ioc.dinjection.provider.factory;

import java.lang.annotation.Annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.ioc.dinjection.Injector;
import lemon.ioc.dinjection.annotations.Singleton;
import lemon.ioc.dinjection.binder.Scope;
import lemon.ioc.dinjection.exception.AnnoHandleException;
import lemon.ioc.dinjection.provider.HandlerChain;
import lemon.ioc.dinjection.provider.InstanceFactory;

/**
 * Handler for IsSingleton annotation. <br>
 * the class would be considered as a singleton.
 *
 * @author lemon
 * @see lemon.ioc.dinjection.annotations.Singleton
 */
public class SingletonFactory implements InstanceFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingletonFactory.class);

    private final Injector injector;

    public SingletonFactory(Injector injector) {
        this.injector = injector;
    }

    @Override
    public boolean canHandle(Annotation[] annotations) {
        for (Annotation ann : annotations) {
            if (ann.annotationType() == Singleton.class) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object handle(Scope scope, Class<?> cls, Class<?> expectedClass, HandlerChain chain) throws AnnoHandleException {
        LOGGER.debug("single with args: \n\tcls:\t{}\n\tchain:\t{}", cls, chain);
        try {
            return chain.next().handle(scope, cls, expectedClass, chain);
        } catch (AnnoHandleException e) {
            LOGGER.debug("start handling with TypeIsSingletonHandler");
            return injector.getObject(scope, cls);
        }
    }

}
