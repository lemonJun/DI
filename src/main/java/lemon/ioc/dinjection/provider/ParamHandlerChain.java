package lemon.ioc.dinjection.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParamHandlerChain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParamHandlerChain.class);

    private final Iterator<ParamHandler> it;

    public ParamHandlerChain(List<ParamHandler> handlers, Annotation[] anns) {
        List<ParamHandler> list = new ArrayList<>();
        handlers.forEach(h -> {
            if (h.canHandle(anns)) {
                list.add(h);
            }
        });
        list.add(EmptyHandler.getInstance());

        LOGGER.debug("Generate Param Chain with Handlers: {}", list);

        it = list.iterator();
    }

    public ParamHandler next() {
        return it.next();
    }
}
