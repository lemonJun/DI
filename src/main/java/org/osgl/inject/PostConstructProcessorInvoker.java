package org.osgl.inject;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Provider;

import org.osgl.inject.util.Tuple;

class PostConstructProcessorInvoker<T> implements Provider<T> {

    private Provider<T> realProvider;
    private List<Tuple<Annotation, PostConstructProcessor<T>>> processors;

    private PostConstructProcessorInvoker(Provider<T> realProvider, List<Tuple<Annotation, PostConstructProcessor<T>>> processors) {
        this.realProvider = realProvider;
        this.processors = processors;
    }

    @Override
    public T get() {
        T t = realProvider.get();
        for (Tuple<Annotation, PostConstructProcessor<T>> pair : processors) {
            pair._2.process(t, pair._1);
        }
        return t;
    }

    static <T> Provider<T> decorate(BeanSpec spec, Provider<T> realProvider, Genie genie) {
        if (realProvider instanceof PostConstructorInvoker) {
            return realProvider;
        }
        Set<Annotation> postProcessors = spec.postProcessors();
        if (postProcessors.isEmpty()) {
            return realProvider;
        }
        List<Tuple<Annotation, PostConstructProcessor<T>>> processors = new ArrayList<Tuple<Annotation, PostConstructProcessor<T>>>(postProcessors.size());
        for (Annotation annotation : postProcessors) {
            try {
                PostConstructProcessor<T> pcp = genie.postConstructProcessor(annotation);
                processors.add(new Tuple(annotation, pcp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new PostConstructProcessorInvoker<T>(realProvider, processors);
    }
}
