package lemon.ioc.di.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a class as Singleton. <br>
 * The system will only have one instance of the class.<br>
 * Note that if the class extends AutoWire, you cannot 'new' the class twice.
 * 
 * @author lemon
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@ScopeAnnotation
public @interface Singleton {
}
