package lemon.needle.ioc;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

import lemon.needle.ioc.exception.NeedleException;

public abstract class AbsModule implements Module {

    private List<Binder> binders = new ArrayList<Binder>();
    private Set<Class<? extends Annotation>> qualifiers = new HashSet<Class<? extends Annotation>>();
    //    private Map<Class<?>, GenericTypedBeanLoader<?>> genericTypedBeanLoaders = new HashMap<Class<?>, GenericTypedBeanLoader<?>>();
    //    private boolean configured;

    protected final <T> Binder<T> bind(Class<T> type) {
        Binder<T> binder = new Binder<T>(type);
        binders.add(binder);
        return binder;
    }

    protected final AbsModule registerQualifiers(Class<? extends Annotation>... qualifiers) {
        this.qualifiers.addAll(Arrays.asList(qualifiers));
        return this;
    }

    public abstract void configure();

    final void applyTo(InjectorImpl injector) {
        configure();
        validate(injector);
        for (Binder<?> binder : binders) {
            binder.register(injector);
        }
    }

    private void validate(InjectorImpl injector) {
        Map<Object, Binder> map = Maps.newConcurrentMap();
        for (Binder<?> binder : binders) {
            Key spec = binder.key(injector);
            if (map.containsKey(spec)) {
                throw new NeedleException("Duplicate bean spec found: ", spec);
            }
            map.put(spec, binder);
        }
    }

}
