package lemon.needle.ioc.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Provider;

/**
 * A pointer to the default provider type for a type.
 *
 * @author crazybob@google.com (Bob Lee)
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface ProvidedBy {

    /**
     * The implementation type.
     */
    Class<? extends Provider<?>> value();
}
