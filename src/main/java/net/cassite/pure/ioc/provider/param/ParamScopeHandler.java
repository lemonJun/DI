package net.cassite.pure.ioc.provider.param;

import net.cassite.pure.ioc.Utils;
import net.cassite.pure.ioc.annotations.ScopeAttr;
import net.cassite.pure.ioc.exception.AnnoHandleException;
import net.cassite.pure.ioc.provider.ParamAnnotationHandler;
import net.cassite.pure.ioc.provider.ParamHandlerChain;
import net.cassite.style.reflect.MemberSup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * handler for annotation @ScopeAttr on parameters
 */
public class ParamScopeHandler implements ParamAnnotationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParamScopeHandler.class);

    @Override
    public boolean canHandle(Annotation[] annotations) {
        return null != Utils.getAnno(ScopeAttr.class, annotations);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object handle(net.cassite.pure.ioc.Scope scope, MemberSup<?> caller, Class<?> cls, Class<?> expectedClass, Annotation[] toHandle, ParamHandlerChain chain) throws AnnoHandleException {
        LOGGER.debug("Entered ParamScopeHandler with args:\n\tcaller:\t{}\n\tcls:\t{}\n\ttoHandle:\t{}\n\tchain:\t{}", caller, cls, toHandle, chain);
        LOGGER.debug("Start handling with ParamScopeHandler");

        ScopeAttr scopeAttrA = Utils.getAnno(ScopeAttr.class, toHandle);
        assert scopeAttrA != null;

        if (scope.containsKey(scopeAttrA.value())) {
            return scope.get(scopeAttrA.value());
        } else {
            Object retrieved = chain.next().handle(scope, caller, cls, expectedClass, toHandle, chain);
            if (scopeAttrA.thread()) {
                net.cassite.pure.ioc.Scope.currentThreadScope().registerInstance(scopeAttrA.value(), retrieved);
            } else {
                scope.registerInstance(scopeAttrA.value(), retrieved);
            }
            return retrieved;
        }
    }
}
