package lemon.ioc.di;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import static net.cassite.style.Style.*;

import net.cassite.style.reflect.MethodSupport;

/**
 * Some useful methods to use when creating annotation handlers.
 *
 * @author lemon
 */
public class Utils {
    protected Utils() {
    }

    /**
     * Retrieve annotation of designated type from given annotations
     *
     * @param annoCls class of the annotation
     * @param annos   annotations to choose from
     * @param <A>     annotation type
     * @return chosen annotation or null if not found.
     */
    @SuppressWarnings("unchecked")
    public static <A extends Annotation> A getAnno(Class<A> annoCls, Annotation[] annos) {
        for (Annotation anno : annos) {
            if (anno.annotationType().equals(annoCls)) {
                return (A) anno;
            }
        }
        return null;
    }

    /**
     * Retrieve annotation of designated type from given annotations
     *
     * @param annoCls class of the annotation
     * @param annos   annotations to choose from
     * @param <A>     annotation type
     * @return chosen annotation or null if not found.
     */
    @SuppressWarnings("unchecked")
    public static <A extends Annotation> A getAnno(Class<A> annoCls, Collection<Annotation> annos) {
        for (Annotation anno : annos) {
            if (anno.annotationType().equals(annoCls)) {
                return (A) anno;
            }
        }
        return null;
    }

    /**
     * retrieve field with given name from given class
     *
     * @param cls       class to retrieve the field from
     * @param fieldName name of the field
     * @return retrieved field
     * @see Class#getDeclaredField(String)
     */
    public static Field getField(Class<?> cls, String fieldName) {
        try {
            return cls.getDeclaredField(fieldName);
        } catch (Exception e) {
            throw $(e);
        }
    }

    /**
     * Retrieve method with 0 parameter count and with given name from given
     * class
     *
     * @param cls  the class to retrieve from
     * @param name name of the method
     * @return retrieved method
     * @see Class#getDeclaredMethod(String, Class...)
     */
    public static Method getMethod(Class<?> cls, String name) {
        return getMethod(cls, name, new Class[0]);
    }

    /**
     * Retrieve method with given name and parameter types from given class
     *
     * @param cls            the class to retrieve from
     * @param name           name of the method
     * @param parameterTypes parameter types
     * @return retrieved method
     * @see Class#getDeclaredMethod(String, Class...)
     */
    public static Method getMethod(Class<?> cls, String name, Class<?>... parameterTypes) {
        try {
            return cls.getDeclaredMethod(name, parameterTypes);
        } catch (Exception e) {
            throw $(e);
        }
    }

    /**
     * the following methods are invoked from IOCController's protected
     * methods, you can use them if you cann't extend IOCController
     */

    /**
     * @param scope         session
     * @param cls           cls
     * @param expectedClass expectedClass
     * @return get
     * @see IOCController#get(Scope, Class, Class)
     */
    //    public static Object get(Scope scope, Class<?> cls, Class<?> expectedClass) {
    //        return IOCController.get(scope, cls, expectedClass);
    //    }

    /**
     * @param scope  session
     * @param target target
     * @param m      m
     * @see Injector#invokeSetter(Scope, Object, MethodSupport)
     */
    //    public static void invokeSetter(Scope scope, Object target, MethodSupport<?, ?> m) {
    //        Injector.invokeSetter(scope, target, m);
    //    }

    /**
     * @param scope session
     * @param cls   cls
     * @return constructObject
     * @see Injector#constructObject(Scope, Class)
     */
    //    public Object constructObject(Scope scope, @SuppressWarnings("rawtypes") Class cls) {
    //        return Injector.constructObject(scope, cls);
    //    }

    /**
     * @param scope session
     * @param cls   cls
     * @return getObject
     * @see Injector#getObject(Scope, Class)
     */
    //    public Object getObject(Scope scope, @SuppressWarnings("rawtypes") Class cls) {
    //        return Injector.getObject(scope, cls);
    //    }

    /**
     * @param scope  session
     * @param method method
     * @param target target
     * @return invokeMethod
     * @see Injector#invokeMethod(Scope, MethodSupport, Object)
     */
    //    public static Object invokeMethod(Scope scope, @SuppressWarnings("rawtypes") MethodSupport method, Object target) {
    //        return Injector.invokeMethod(scope, method, target);
    //    }

    /**
     * determine whether method is a setter
     *
     * @param method method
     * @return true if it's a setter, false otherwise
     */
    public static boolean isSetter(MethodSupport method) {
        return method.name().startsWith("set") && method.name().length() > 3 && method.name().charAt(3) >= 'A' && method.name().charAt(3) <= 'Z' && method.argCount() == 1 && (method.returnType().equals(Void.TYPE) || method.returnType().equals(method.getMember().getDeclaringClass())) && !method.isStatic();
    }
}
