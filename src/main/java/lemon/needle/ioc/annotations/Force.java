package lemon.needle.ioc.annotations;

import java.lang.annotation.*;

/**
 * 
 * Force a setter or one of method's parameter to use the given value.<br>
 * The system will try to transform the value into proper type.<br>
 * Only use this method on those parameters/setters which types are primitive or
 * String
 *
 * @author lemon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Inherited
public @interface Force {
    String value();

    /**
     * use property value as the value to inject( value would be considered as key of the property )
     *
     * @return properties name
     * @since 0.2.2
     */
    String properties() default "";
}
