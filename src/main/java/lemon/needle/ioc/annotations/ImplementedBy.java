package lemon.needle.ioc.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A pointer to the default implementation of a type.
 *
 * @author crazybob@google.com (Bob Lee)
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface ImplementedBy {

    /** The implementation type. */
    Class<?> value();
}
