package net.cassite.pure.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables AOP feature of the class
 *
 * @author lemon
 * @since 0.1.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AOP {
    /**
     * @return net.cassite.pure.aop.Weaver
     * @see net.cassite.pure.aop.Weaver
     */
    @SuppressWarnings("rawtypes")
    Class[] value();

    boolean useCglib() default false;

    long timeoutMillis() default 0L;
}
