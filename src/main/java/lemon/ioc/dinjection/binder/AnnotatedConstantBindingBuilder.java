package lemon.ioc.dinjection.binder;

import java.lang.annotation.Annotation;

/**
 * See the EDSL examples at {@link com.google.inject.Binder}.
 *
 * @author crazybob@google.com (Bob Lee)
 */
public interface AnnotatedConstantBindingBuilder {

    /**
     * See the EDSL examples at {@link com.google.inject.Binder}.
     */
    ConstantBindingBuilder annotatedWith(Class<? extends Annotation> annotationType);

    /**
     * See the EDSL examples at {@link com.google.inject.Binder}.
     */
    ConstantBindingBuilder annotatedWith(Annotation annotation);
}
