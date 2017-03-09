package lemon.ioc.dinjection.provider.param;
//package net.cassite.pure.ioc.provider.param;
//
//import java.lang.annotation.Annotation;
//import java.util.Arrays;
//
//import net.cassite.pure.ioc.*;
//import net.cassite.pure.ioc.annotations.Extend;
//import net.cassite.pure.ioc.provider.IrrelevantAnnotationHandlingException;
//import net.cassite.pure.ioc.provider.ParamAnnotationHandler;
//import net.cassite.pure.ioc.provider.ParamHandlerChain;
//import net.cassite.style.reflect.MemberSup;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * Handler for Extend annotation<br>
// * Simplifies the process of retreiving objects from other object factories
// *
// * @author lemon
// */
//public class ParamExtendHandler implements ParamAnnotationHandler {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ParamExtendHandler.class);
//
//    private final Injector injector;
//
//    public ParamExtendHandler(Injector injector) {
//        this.injector = injector;
//    }
//
//    @Override
//    public boolean canHandle(Annotation[] annotations) {
//        return null != Utils.getAnno(Extend.class, annotations);
//    }
//
//    @Override
//    public Object handle(Scope scope, MemberSup<?> caller, Class<?> cls, Class<?> expectedClass, Annotation[] toHandle, ParamHandlerChain chain) throws AnnoHandleException {
//        LOGGER.debug("Entered ParamExtendHandler with args:\n\tcaller:\t{}\n\tcls:\t{}\n\ttoHandle:\t{}\n\tchain:\t{}", caller, cls, toHandle, chain);
//        try {
//            return chain.next().handle(scope, caller, cls, expectedClass, toHandle, chain);
//        } catch (IrrelevantAnnotationHandlingException e) {
//            LOGGER.debug("Start handling with ParamExtendHandler");
//
//            Extend extend = Utils.getAnno(Extend.class, toHandle);
//            assert extend != null;
//            @SuppressWarnings("unchecked")
//            ExtendingHandler handler = (ExtendingHandler) injector.getInstance(scope, extend.handler());
//
//            LOGGER.debug("--retrieved extend handler is {}, filling in args {}", handler, Arrays.toString(extend.args()));
//
//            return handler.get(extend.args());
//        }
//    }
//
//}
