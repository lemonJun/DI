package net.cassite.pure.ioc.annotations;

import java.lang.annotation.*;

/**
 * All setters will be invoked, but you can add Ignore to the setters you don't
 * want the system to invoke.
 * 
 * @author lemon
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Inherited
public @interface Ignore {
}
