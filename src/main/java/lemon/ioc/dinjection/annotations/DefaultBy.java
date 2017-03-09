package lemon.ioc.dinjection.annotations;

import java.lang.annotation.*;

/**
 * Annotation used to determine the default constructor, <b>or</b> the class
 * redirected to.<br>
 * e.g.<br>
 * You have two constructors, both with parameters, it's ambiguous to determine
 * which one to invoke.<br>
 * Use Default annotation to set a default one. <br>
 * <br>
 * e.g.<br>
 * You have an interface or abstract class, you want to construct an
 * implementation, and there're a lot of work to do if you add Use annotation to
 * each of setters. You can use Default on the interface or abstract class. The
 * system would <b>redirect</b> the constructing target to the one you chose.
 * 
 * @author lemon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.CONSTRUCTOR })
@Inherited
public @interface DefaultBy {
    /**
     * redirect the construction to another class
     *
     * @return class
     */
    @SuppressWarnings("rawtypes")
    Class value() default DefaultBy.class;
}
