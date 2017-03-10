package lemon.needle.ioc.provider.param;

import net.cassite.style.reflect.MemberSup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.needle.ioc.annotations.ScopeAttr;
import lemon.needle.ioc.exception.AnnoHandleException;
import lemon.needle.ioc.provider.ParamHandler;
import lemon.needle.ioc.provider.ParamHandlerChain;
import lemon.needle.ioc.util.Utils;

import java.lang.annotation.Annotation;

/**
 * handler for annotation @ScopeAttr on parameters
 */
public class ParamScopeHandler implements ParamHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParamScopeHandler.class);

    @Override
    public boolean canHandle(Annotation[] annotations) {
        return null != Utils.getAnno(ScopeAttr.class, annotations);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object handle(lemon.needle.ioc.binder.Scope scope, MemberSup<?> caller, Class<?> cls, Class<?> expectedClass, Annotation[] toHandle, ParamHandlerChain chain) throws AnnoHandleException {
        LOGGER.debug("Entered ParamScopeHandler with args:\n\tcaller:\t{}\n\tcls:\t{}\n\ttoHandle:\t{}\n\tchain:\t{}", caller, cls, toHandle, chain);
        LOGGER.debug("Start handling with ParamScopeHandler");

        ScopeAttr scopeAttrA = Utils.getAnno(ScopeAttr.class, toHandle);
        assert scopeAttrA != null;

        if (scope.containsKey(scopeAttrA.value())) {
            return scope.get(scopeAttrA.value());
        } else {
            Object retrieved = chain.next().handle(scope, caller, cls, expectedClass, toHandle, chain);
            if (scopeAttrA.thread()) {
                lemon.needle.ioc.binder.Scope.currentThreadScope().registerInstance(scopeAttrA.value(), retrieved);
            } else {
                scope.registerInstance(scopeAttrA.value(), retrieved);
            }
            return retrieved;
        }
    }
}
