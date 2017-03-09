package lemon.ioc.di.annotations;

import java.lang.annotation.*;

/**
 * indicate that the instance will be shared in one construction process
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ScopeAttr {
    /**
     * name of registered instance in scope
     *
     * @return name
     */
    String value();

    /**
     * the value would be put into the thread scope if true
     *
     * @return true/false
     * @since 0.3.1
     */
    boolean thread() default false;
}
