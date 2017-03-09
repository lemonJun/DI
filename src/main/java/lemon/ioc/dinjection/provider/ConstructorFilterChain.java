package lemon.ioc.dinjection.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ConstructorFilterChain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstructorFilterChain.class);

    private final Iterator<ConstructorFilter> it;

    public ConstructorFilterChain(List<ConstructorFilter> handlers, Set<Annotation> anns) {

        List<ConstructorFilter> list = new ArrayList<>();
        handlers.forEach(e -> {
            if (e.canHandle(anns)) {
                list.add(e);
            }
        });
        list.add(EmptyHandler.getInstance());

        LOGGER.debug("Generate Constructor Chain with Filters: {}", list);

        it = list.iterator();
    }

    public ConstructorFilter next() {
        return it.next();
    }
}
