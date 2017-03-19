package lemon.needle.aop;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import javax.inject.Inject;

import lemon.needle.ioc.TypeLiteral;

/**
 * A constructor, field or method that can receive injections. Typically this is a member with the
 * {@literal @}{@link Inject} annotation. For non-private, no argument constructors, the member may
 * omit the annotation.
 *
 * @author crazybob@google.com (Bob Lee)
 * @since 2.0
 */
public final class InjectionPoint {

    private static final Logger logger = Logger.getLogger(InjectionPoint.class.getName());

    private final boolean optional;
    private final Member member;
    private final TypeLiteral<?> declaringType;

    InjectionPoint(TypeLiteral<?> declaringType, Method method, boolean optional) {
        this.member = method;
        this.declaringType = declaringType;
        this.optional = optional;
    }

    InjectionPoint(TypeLiteral<?> declaringType, Constructor<?> constructor) {
        this.member = constructor;
        this.declaringType = declaringType;
        this.optional = false;
    }

    /** Returns the injected constructor, field, or method. */
    public Member getMember() {
        return member;
    }

    /**
     * Returns true if this injection point shall be skipped if the injector cannot resolve bindings
     * for all required dependencies. Both explicit bindings (as specified in a module), and implicit
     * bindings ({@literal @}{@link com.google.inject.ImplementedBy ImplementedBy}, default
     * constructors etc.) may be used to satisfy optional injection points.
     */
    public boolean isOptional() {
        return optional;
    }

    /**
     * Returns the generic type that defines this injection point. If the member exists on a
     * parameterized type, the result will include more type information than the member's {@link
     * Member#getDeclaringClass() raw declaring class}.
     *
     * @since 3.0
     */
    public TypeLiteral<?> getDeclaringType() {
        return declaringType;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof InjectionPoint && member.equals(((InjectionPoint) o).member) && declaringType.equals(((InjectionPoint) o).declaringType);
    }

    @Override
    public int hashCode() {
        return member.hashCode() ^ declaringType.hashCode();
    }

    /**
     * Returns a new injection point for the specified constructor. If the declaring type of {@code
     * constructor} is parameterized (such as {@code List<T>}), prefer the overload that includes a
     * type literal.
     *
     * @param constructor any single constructor present on {@code type}.
     * @since 3.0
     */
    public static <T> InjectionPoint forConstructor(Constructor<T> constructor) {
        return new InjectionPoint(TypeLiteral.get(constructor.getDeclaringClass()), constructor);
    }

}
