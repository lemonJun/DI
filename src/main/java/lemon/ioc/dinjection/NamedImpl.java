package lemon.ioc.dinjection;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import lemon.ioc.dinjection.annotations.Named;

class NamedImpl implements Named, Serializable {

    private final String value;

    public NamedImpl(String value) {
        this.value = checkNotNull(value, "name");
    }

    public String value() {
        return this.value;
    }

    public int hashCode() {
        // This is specified in java.lang.Annotation.
        return (127 * "value".hashCode()) ^ value.hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof Named)) {
            return false;
        }

        Named other = (Named) o;
        return value.equals(other.value());
    }

    public String toString() {
        return "@" + Named.class.getName() + "(value=" + value + ")";
    }

    public Class<? extends Annotation> annotationType() {
        return Named.class;
    }

    private static final long serialVersionUID = 0;
}
