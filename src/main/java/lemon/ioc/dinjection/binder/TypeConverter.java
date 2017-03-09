
package lemon.ioc.dinjection.binder;

import lemon.ioc.dinjection.TypeLiteral;

/**
 * Converts constant string values to a different type.
 *
 * @author crazybob@google.com (Bob Lee)
 * @since 2.0
 */
public interface TypeConverter {

    /**
     * Converts a string value. Throws an exception if a conversion error occurs.
     */
    Object convert(String value, TypeLiteral<?> toType);
}
