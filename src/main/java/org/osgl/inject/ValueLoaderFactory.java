package org.osgl.inject;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.inject.Provider;

import org.osgl.inject.annotation.LoadValue;
import org.osgl.inject.util.CommonUtil;

import com.google.common.base.Preconditions;

/**
 * `ValueLoaderFactory` load the bean instance directly from
 * {@link ValueLoader} based on the option data specified in
 * {@link org.osgl.inject.annotation.LoadValue} annotation
 */
class ValueLoaderFactory {

    static <T> Provider<T> create(BeanSpec spec, Genie genie) {
        Annotation anno = spec.valueLoader();
        Preconditions.checkNotNull(anno);
        //        E.illegalArgumentIf(null == anno);
        Map<String, Object> options = CommonUtil.evaluate(anno);
        Class<? extends Annotation> annoType = anno.annotationType();
        LoadValue loadValue;
        if (LoadValue.class == annoType) {
            loadValue = (LoadValue) anno;
        } else {
            loadValue = annoType.getAnnotation(LoadValue.class);
        }
        ValueLoader<T> valueLoader = genie.get(loadValue.value());
        valueLoader.init(options, spec);
        return valueLoader;
    }

}
