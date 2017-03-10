package lemon.needle.ioc.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 以链式的方式去处理   好像并没有分清scope和binder和属性的概念
 * 
 * @author WangYazhou
 * @date  2017年3月9日 下午1:15:40
 * @see
 */
public class HandlerChain {

    private static final Logger logger = LoggerFactory.getLogger(HandlerChain.class);

    private final Iterator<InstanceFactory> it;

    public HandlerChain(List<InstanceFactory> handlers, Annotation[] anns) {

        List<InstanceFactory> list = new ArrayList<>();
        handlers.forEach(h -> {
            if (h.canHandle(anns)) {
                list.add(h);
            }
        });
        list.add(EmptyHandler.getInstance());

        logger.debug("Generate Type Chain with handlers {}", list);
        
        it = list.iterator();
    }
    
    public InstanceFactory next() {
        return it.next();
    }
}
