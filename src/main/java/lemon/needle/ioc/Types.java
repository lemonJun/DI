package lemon.needle.ioc;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Provider;

import lemon.needle.util.MoreTypes;
import lemon.needle.util.MoreTypes.GenericArrayTypeImpl;
import lemon.needle.util.MoreTypes.ParameterizedTypeImpl;
import lemon.needle.util.MoreTypes.WildcardTypeImpl;

/**
 * Static methods for working with types.
 *
 * @author crazybob@google.com (Bob Lee)
 * @since 2.0
 */
public final class Types {
    private Types() {
    }

    /**
     * Returns a new parameterized type, applying {@code typeArguments} to
     * {@code rawType}. The returned type does not have an owner type.
     *
     * @return a {@link java.io.Serializable serializable} parameterized type.
     */
    public static ParameterizedType newParameterizedType(Type rawType, Type... typeArguments) {
        return newParameterizedTypeWithOwner(null, rawType, typeArguments);
    }

    /**
     * Returns a new parameterized type, applying {@code typeArguments} to
     * {@code rawType} and enclosed by {@code ownerType}.
     *
     * @return a {@link java.io.Serializable serializable} parameterized type.
     */
    public static ParameterizedType newParameterizedTypeWithOwner(Type ownerType, Type rawType, Type... typeArguments) {
        return new ParameterizedTypeImpl(ownerType, rawType, typeArguments);
    }

    /**
     * Returns an array type whose elements are all instances of
     * {@code componentType}.
     *
     * @return a {@link java.io.Serializable serializable} generic array type.
     */
    public static GenericArrayType arrayOf(Type componentType) {
        return new GenericArrayTypeImpl(componentType);
    }

    /**
     * Returns a type that represents an unknown type that extends {@code bound}.
     * For example, if {@code bound} is {@code CharSequence.class}, this returns
     * {@code ? extends CharSequence}. If {@code bound} is {@code Object.class},
     * this returns {@code ?}, which is shorthand for {@code ? extends Object}.
     */
    public static WildcardType subtypeOf(Type bound) {
        return new WildcardTypeImpl(new Type[] { bound }, MoreTypes.EMPTY_TYPE_ARRAY);
    }

    /**
     * Returns a type that represents an unknown supertype of {@code bound}. For
     * example, if {@code bound} is {@code String.class}, this returns {@code ?
     * super String}.
     */
    public static WildcardType supertypeOf(Type bound) {
        return new WildcardTypeImpl(new Type[] { Object.class }, new Type[] { bound });
    }

    /**
     * Returns a type modelling a {@link List} whose elements are of type
     * {@code elementType}.
     *
     * @return a {@link java.io.Serializable serializable} parameterized type.
     */
    public static ParameterizedType listOf(Type elementType) {
        return newParameterizedType(List.class, elementType);
    }

    /**
     * Returns a type modelling a {@link Set} whose elements are of type
     * {@code elementType}.
     *
     * @return a {@link java.io.Serializable serializable} parameterized type.
     */
    public static ParameterizedType setOf(Type elementType) {
        return newParameterizedType(Set.class, elementType);
    }

    /**
     * Returns a type modelling a {@link Map} whose keys are of type
     * {@code keyType} and whose values are of type {@code valueType}.
     *
     * @return a {@link java.io.Serializable serializable} parameterized type.
     */
    public static ParameterizedType mapOf(Type keyType, Type valueType) {
        return newParameterizedType(Map.class, keyType, valueType);
    }

    // for other custom collections types, use newParameterizedType()

    /**
     * Returns a type modelling a {@link Provider} that provides elements of type
     * {@code elementType}.
     *
     * @return a {@link java.io.Serializable serializable} parameterized type.
     */
    public static ParameterizedType providerOf(Type providedType) {
        return newParameterizedType(Provider.class, providedType);
    }
}