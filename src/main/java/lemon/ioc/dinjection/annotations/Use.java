package lemon.ioc.dinjection.annotations;

import java.lang.annotation.*;

/**
 * Force a setter or one of method's parameter to use the given class's instance
 * / constant / variable.<br>
 * It's similar to Force but it is not limited to primitives or Strings.<br>
 * Constants and variables can be registered in IOCController
 *
 * @author lemon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Inherited
public @interface Use {
    /**
     * use instance of designated class
     *
     * @return class
     */
    @SuppressWarnings("rawtypes")
    Class cls() default Use.class;

    /**
     * use instance registered in scope
     *
     * @return name
     */
    String value() default "";
}
