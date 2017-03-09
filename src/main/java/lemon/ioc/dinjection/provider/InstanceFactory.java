package lemon.ioc.dinjection.provider;

import java.lang.annotation.Annotation;

import lemon.ioc.dinjection.binder.Scope;
import lemon.ioc.dinjection.exception.AnnoHandleException;

/**
 * 根据类型检索其对应的实例
 * retrieve an instance of given type.
 *
 * @author lemon
 */
public interface InstanceFactory {
    /**
     * one of given annotations can be handled
     *
     * @param annotations a summary of annotations
     * @return true if one of the annos can be handled, false otherwise.
     */
    boolean canHandle(Annotation[] annotations);

    /**
     * retrieve an instance of given type.
     *
     * @param scope         wire scope
     * @param cls           instance of which to retrieve
     * @param expectedClass originally wanted type
     * @param chain         the Type Chain<br>
     *                      Usually call
     *                      <code>chain.next().handle(cls, chain)</code> before do
     *                      real handling.<br>
     *                      If the <code>next()</code> handler has a result of
     *                      <b>not null</b>, usually the handler simply return
     *                      result.
     * @return retrieved instance
     * @throws AnnoHandleException IrrelevantAnnotationHandlingException is thrown when it fails to retrieve corresponding object
     */
    Object handle(Scope scope, Class<?> cls, Class<?> expectedClass, HandlerChain chain) throws AnnoHandleException;
}
