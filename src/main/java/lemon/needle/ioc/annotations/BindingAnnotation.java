package lemon.needle.ioc.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotates annotations which are used for binding. Only one such annotation
 * may apply to a single injection point. You must also annotate binder
 * annotations with {@code @Retention(RUNTIME)}. For example:
 *
 * <pre>
 *   {@code @}Retention(RUNTIME)
 *   {@code @}Target({ FIELD, PARAMETER, METHOD })
 *   {@code @}BindingAnnotation
 *   public {@code @}interface Transactional {}
 * </pre>
 *
 * @author crazybob@google.com (Bob Lee)
 */
@Target(ANNOTATION_TYPE)
@Retention(RUNTIME)
public @interface BindingAnnotation {
}
