package net.cassite.pure.ioc.provider.factory;

import java.lang.annotation.Annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.cassite.pure.ioc.Injector;
import net.cassite.pure.ioc.Scope;
import net.cassite.pure.ioc.annotations.DefaultBy;
import net.cassite.pure.ioc.exception.AnnoHandleException;
import net.cassite.pure.ioc.provider.HandlerChain;
import net.cassite.pure.ioc.provider.IrrelevantAnnotationHandlingException;
import net.cassite.pure.ioc.provider.TypeAnnotationHandler;

/**
 * Handler for Default annotation. <br>
 * Instantiate the class that 'Default' annotation represents.
 *
 * @author lemon
 * @see net.cassite.pure.ioc.annotations.DefaultBy
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
