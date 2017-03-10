package lemon.needle.ioc.provider.factory;

import java.lang.annotation.Annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.needle.ioc.Injector;
import lemon.needle.ioc.annotations.DefaultBy;
import lemon.needle.ioc.binder.Scope;
import lemon.needle.ioc.exception.AnnoHandleException;
import lemon.needle.ioc.provider.HandlerChain;
import lemon.needle.ioc.provider.InstanceFactory;
import lemon.needle.ioc.provider.IrrelevantAnnoException;

/**
 * Handler for Default annotation. <br>
 * Instantiate the class that 'Default' annotation represents.
 *
 * @author lemon
 * @see lemon.needle.ioc.annotations.DefaultBy
 */
public class ImplementedByFactory implements InstanceFactory {
    private static final Logger logger = LoggerFactory.getLogger(ImplementedByFactory.class);

    private final Injector injector;

    public ImplementedByFactory(Injector injector) {
        this.injector = injector;
        TypeWireHandler.ignoreWhenMeetingAnnotation(DefaultBy.class);

    }

    @Override
    public boolean canHandle(Annotation[] annotations) {
        for (Annotation ann : annotations) {
            if (ann.annotationType() == DefaultBy.class) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object handle(Scope scope, Class<?> cls, Class<?> expectedClass, HandlerChain chain) throws AnnoHandleException {
        logger.debug("Entered TypeDefaultHandler with args: \n\tcls:\t{}\n\tchain:\t{}", cls, chain);
        try {
            return chain.next().handle(scope, cls, expectedClass, chain);
        } catch (IrrelevantAnnoException e) {
            logger.debug("start handling with TypeDefaultHandler");

            DefaultBy ann = cls.getAnnotation(DefaultBy.class);

            Class<?> clazz = ann.value();

            logger.debug("--Redirecting to class " + clazz);
            logger.debug("Invoking get(Class) ...");
            return injector.getInstance(scope, clazz, expectedClass);
        }
    }

}
