
package lemon.ioc.dinjection.binder;

import java.lang.annotation.Annotation;

/**
 * See the EDSL examples at {@link com.google.inject.Binder}.
 *
 * @author jessewilson@google.com (Jesse Wilson)
 * @since 2.0
 */
public interface AnnotatedElementBuilder {

    /**
     * See the EDSL examples at {@link com.google.inject.Binder}.
     */
    void annotatedWith(Class<? extends Annotation> annotationType);

    /**
     * See the EDSL examples at {@link com.google.inject.Binder}.
     */
    void annotatedWith(Annotation annotation);
}
