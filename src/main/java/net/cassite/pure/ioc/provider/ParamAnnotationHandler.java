package net.cassite.pure.ioc.provider;

import java.lang.annotation.Annotation;

import net.cassite.pure.ioc.Scope;
import net.cassite.pure.ioc.exception.AnnoHandleException;
import net.cassite.style.reflect.MemberSup;

/**
 * retrieve an instance of given class
 *
 * @author lemon
 */
public interface ParamAnnotationHandler {
    /**
     * one of given annotations can be handled
     *
     * @param annotations a summary of annotations
     * @return true if one of the annos can be handled, false otherwise.
     */
    boolean canHandle(Annotation[] annotations);

    /**
     * retrieve an instance of given <code>cls</code>
     *
     * @param scope         wire scope
     * @param caller        the member calling for arguments
     * @param cls           class of instance to retrieve
     * @param expectedClass the class originally expected
     * @param toHandle      annotations
     * @param chain         the Param Chain<br>
     *                      Usually call
     *                      <code>chain.next().handle(caller, cls, toHandle, chain)</code>
     *                      before do real handling.<br>
     *                      The <code>next()</code> handler may throw
     *                      <code>IrrelevantAnnotationHandlingException</code>, it
     *                      means the <code>next()</code> handler failed
     *                      retrieving instance, and need current handler do
     *                      handling.
     * @return retrieved object to fill the param
     * @throws AnnoHandleException IrrelevantAnnotationHandlingException is thrown when it fails to retrieve object for the parameter
     */
    Object handle(Scope scope, MemberSup<?> caller, Class<?> cls, Class<?> expectedClass, Annotation[] toHandle, ParamHandlerChain chain) throws AnnoHandleException;
}
