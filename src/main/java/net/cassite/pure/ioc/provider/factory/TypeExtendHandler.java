//package net.cassite.pure.ioc.provider.factory;
//
//import java.lang.annotation.Annotation;
//import java.util.Arrays;
//
//import net.cassite.pure.ioc.*;
//import net.cassite.pure.ioc.annotations.Extend;
//import net.cassite.pure.ioc.provider.HandlerChain;
//import net.cassite.pure.ioc.provider.IrrelevantAnnotationHandlingException;
//import net.cassite.pure.ioc.provider.TypeAnnotationHandler;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * Handler of Extend annotation
// *
// * @author lemon
// */
//public class TypeExtendHandler implements TypeAnnotationHandler {
//
//    private final Injector injector;
//
//    public TypeExtendHandler(Injector injector) {
//        this.injector = injector;
//    }
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(TypeExtendHandler.class);
//
//    @Override
//    public boolean canHandle(Annotation[] annotations) {
//        return null != Utils.getAnno(Extend.class, annotations);
//    }
//
//    @Override
//    public Object handle(Scope scope, Class<?> cls, Class<?> expectedClass, HandlerChain chain) throws AnnoHandleException {
//        LOGGER.debug("Entered TypeExtendHandler with args: \n\tcls:\t{}\n\tchain:\t{}", cls, chain);
//
//        try {
//            return chain.next().handle(scope, cls, expectedClass, chain);
//        } catch (IrrelevantAnnotationHandlingException e) {
//            LOGGER.debug("Start handling with TypeExtendHandler");
//
//            Extend extend = Utils.getAnno(Extend.class, cls.getAnnotations());
//            assert extend != null;
//            @SuppressWarnings("unchecked")
//            ExtendingHandler handler = (ExtendingHandler) injector.getInstance(scope, extend.handler());
//
//            LOGGER.debug("--retrieved extend handler is " + handler + ", filling in args " + Arrays.toString(extend.args()));
//
//            return handler.get(extend.args());
//        }
//    }
//
//}
