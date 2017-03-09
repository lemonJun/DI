package lemon.ioc.dinjection.annotations;

import java.lang.annotation.*;

/**
 * Invoke a method on construction.
 * 
 * @author lemon
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Invoke {
}
