package lemon.needle.ioc;

import java.lang.reflect.Field;

import javax.inject.Provider;

import lemon.needle.ioc.exception.NeedleException;

public class FieldInjector {
    private final Field field;
    private final Key<?> key;
    private final Provider<?> provider;

    public FieldInjector(Field field, Key<?> key, Provider<?> provider) {
        this.field = field;
        this.key = key;
        this.provider = provider;
    }

    public void applyTo(Object bean) {
        Object obj = provider.get();
        if (null == obj) {
            return;
        }
        try {
            field.set(bean, obj);
        } catch (Exception e) {
            throw new NeedleException(e, "Unable to inject field value on %s", bean.getClass());
        }
    }

    @Override
    public String toString() {
        return String.format("Field for %s", field);
    }

    public Field getField() {
        return field;
    }

    public Key getKey() {
        return key;
    }

    public Provider getProvider() {
        return provider;
    }

}
