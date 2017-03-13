package org.osgl.inject;

import java.lang.reflect.Field;

import javax.inject.Provider;

public class FieldInjector {
    private final Field field;
    private final BeanSpec fieldSpec;
    private final Provider provider;

    FieldInjector(Field field, BeanSpec fieldSpec, Provider provider) {
        this.field = field;
        this.fieldSpec = fieldSpec;
        this.provider = provider;
    }

    void applyTo(Object bean) {
        Object obj = provider.get();
        if (null == obj) {
            return;
        }
        try {
            field.set(bean, obj);
        } catch (Exception e) {
            throw new InjectException(e, "Unable to inject field value on %s", bean.getClass());
        }
    }

    @Override
    public String toString() {
        return String.format("Field for %s", field);
    }

    public Field getField() {
        return field;
    }

    public BeanSpec getFieldSpec() {
        return fieldSpec;
    }

    public Provider getProvider() {
        return provider;
    }

}
