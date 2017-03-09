package lemon.ioc.di.provider;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import lemon.ioc.di.Scope;
import lemon.ioc.di.exception.AnnoHandleException;
import net.cassite.style.reflect.ConstructorSup;

/**
 * choose a constructor
 *
 * @author lemon
 */
public interface ConstructorFilter {
    /**
     * one of given annotations can be handled
     *
     * @param annotations a summary of annotations
     * @return true if one of the annos can be handled, false otherwise.
     */
    boolean canHandle(Set<Annotation> annotations);

    /**
     * choose a Constructor to invoke
     *
     * @param scope wire scope
     * @param cons    constructors to choose from
     * @param chain   the Constructor chain<br>
     *                usually invoke
     *                <code>chain.next().handle(cons, chain)</code> and
     *                check return value before do real handling.<br>
     *                If the <code>next()</code> handler has a result of
     *                <b>not null</b>, usually the handler simply return
     *                result.
     * @return the chosen constructor
     * @throws AnnoHandleException exception
     */
    ConstructorSup<Object> handle(Scope scope, List<ConstructorSup<Object>> cons, ConstructorFilterChain chain) throws AnnoHandleException;
}
