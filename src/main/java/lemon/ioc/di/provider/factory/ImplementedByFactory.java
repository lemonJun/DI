package lemon.ioc.di.provider.factory;

import java.lang.annotation.Annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.ioc.di.Injector;
import lemon.ioc.di.annotations.DefaultBy;
import lemon.ioc.di.binder.Scope;
import lemon.ioc.di.exception.AnnoHandleException;
import lemon.ioc.di.provider.HandlerChain;
import lemon.ioc.di.provider.IrrelevantAnnotationHandlingException;
import lemon.ioc.di.provider.TypeAnnotationHandler;

/**
 * Handler for Default annotation. <br>
 * Instantiate the class that 'Default' annotation represents.
 *
 * @author lemon
 * @see lemon.ioc.di.annotations.DefaultBy
 */
public class ImplementedByFactory implements TypeAnnotationHandler {
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
        } catch (IrrelevantAnnotationHandlingException e) {
            logger.debug("start handling with TypeDefaultHandler");

            DefaultBy ann = cls.getAnnotation(DefaultBy.class);

            Class<?> clazz = ann.value();

            logger.debug("--Redirecting to class " + clazz);
            logger.debug("Invoking get(Class) ...");
            return injector.getInstance(scope, clazz, expectedClass);
        }
    }

}
