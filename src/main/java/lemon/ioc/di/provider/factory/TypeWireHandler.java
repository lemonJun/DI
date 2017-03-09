package lemon.ioc.di.provider.factory;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.ioc.di.Injector;
import lemon.ioc.di.Scope;
import lemon.ioc.di.exception.AnnoHandleException;
import lemon.ioc.di.exception.ConstructingMultiSingletonException;
import lemon.ioc.di.provider.HandlerChain;
import lemon.ioc.di.provider.TypeAnnotationHandler;
import net.cassite.style.Style;
import net.cassite.style.aggregation.Aggregation;

/**
 * 
 * Handler for Wire annotation. <br>
 * if the class extends from AutoWire, this would simply return <br>
 * else, invoke AutoWire.wire with current scope and retrieved object
 * 
 * 
 * @author lemon
 * @see Wire
 */
public class TypeWireHandler implements TypeAnnotationHandler {

    private static final Logger logger = LoggerFactory.getLogger(TypeWireHandler.class);
    private static final Set<Class<? extends Annotation>> ignoringAnnotations = new HashSet<>();

    private final Injector injector;

    public TypeWireHandler(Injector injector) {
        this.injector = injector;
    }

    public static void ignoreWhenMeetingAnnotation(Class<? extends Annotation> anno) {
        ignoringAnnotations.add(anno);
    }

    @Override
    public boolean canHandle(Annotation[] annotations) {
        return true;
    }

    @Override
    public Object handle(Scope scope, Class<?> cls, Class<?> expectedClass, HandlerChain chain) throws AnnoHandleException {
        logger.debug("Entered TypeWireHandler with args: \n\tcls:\t{}\n\tchain:\t{}", cls, chain);
        Object inst = chain.next().handle(scope, cls, expectedClass, chain);

        //        if (!AutoWire.class.isAssignableFrom(cls) && Style.avoidNull(Aggregation.$(ignoringAnnotations).forEach(ac -> {
        if (Style.avoidNull(Aggregation.$(ignoringAnnotations).forEach(ac -> {
            if (cls.isAnnotationPresent(ac))
                return Style.BreakWithResult(false);
            return true;
        }), true)) {
            logger.debug("start handling with TypeWireHandler");
            try {
                injector.wire(scope, inst);
            } catch (ConstructingMultiSingletonException ignore) {
            }
        }
        return inst;
    }

}
