package org.osgl.inject.loader;

import org.osgl.inject.BeanSpec;
import org.osgl.inject.InjectException;
import org.osgl.inject.ValueLoader;
import org.osgl.inject.util.CommonUtil;
import org.osgl.inject.util.StringValueResolver;

import lemon.needle.ioc.util.StringUtil;

/**
 * Load value from configuration source
 */
public abstract class ConfigurationValueLoader<T> extends ValueLoader.Base<T> {

    @Override
    public T get() {
        String confKey = value();
        if (StringUtil.isNullOrEmpty(confKey)) {
            throw new InjectException(("Missing configuration key"));
        }
        Object conf = conf(confKey);
        if (null == conf) {
            return null;
        }
        return cast(conf, spec);
    }

    /**
     * Return whatever configured by `key`
     * @param key the configuration key
     * @return the configured value
     */
    protected abstract Object conf(String key);

    private T cast(Object val, BeanSpec spec) {
        Class<?> type = spec.rawType();
        if (type.isInstance(val)) {
            return (T) val;
        }
        if (CommonUtil.isSimpleType(type)) {
            StringValueResolver svr = StringValueResolver.predefined(type);
            if (null != svr) {
                return (T) svr.resolve(val == null ? "" : val.toString());
            }
        }
        throw new InjectException(String.format("Cannot cast value type[%s] to required type[%]", val.getClass(), type));
    }
}
