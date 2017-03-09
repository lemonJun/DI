package net.cassite.pure.ioc.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 *
 * @author WangYazhou
 * @date  2017年3月9日 下午1:15:40
 * @see
 */
public class HandlerChain {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerChain.class);

    private final Iterator<TypeAnnotationHandler> it;

    public HandlerChain(List<TypeAnnotationHandler> handlers, Annotation[] anns) {

        List<TypeAnnotationHandler> list = new ArrayList<>();
        handlers.forEach(h -> {
            if (h.canHandle(anns)) {
                list.add(h);
            }
        });
        list.add(EmptyHandler.getInstance());

        LOGGER.debug("Generate Type Chain with handlers {}", list);

        it = list.iterator();
    }

    public TypeAnnotationHandler next() {
        return it.next();
    }
}
