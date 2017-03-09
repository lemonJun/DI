package lemon.ioc.dinjection.provider.constructor;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import static net.cassite.style.Style.*;
import static net.cassite.style.aggregation.Aggregation.*;

import net.cassite.style.reflect.ConstructorSup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.ioc.dinjection.binder.Scope;
import lemon.ioc.dinjection.exception.AnnoHandleException;
import lemon.ioc.dinjection.provider.ConstructorFilter;
import lemon.ioc.dinjection.provider.ConstructorFilterChain;

/**
 * <br>
 * Default implementation of ConstructorFilter <br>
 * If only one constructor exists , return the constructor. <br>
 * If more than one constructor exists, <br>
 * ____if contains a constructor with no parameters, return this constructor.
 * <br>
 * ____else throw exception.
 *
 * @author lemon
 */
public class DefaultConstructorFilter implements ConstructorFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConstructorFilter.class);

    @Override
    public boolean canHandle(Set<Annotation> annotations) {
        return true;
    }

    @Override
    public ConstructorSup<Object> handle(Scope scope, List<ConstructorSup<Object>> cons, ConstructorFilterChain chain) throws AnnoHandleException {
        LOGGER.debug("Entered DefaultConstructorFilter with args:\n\tcons:\t{}\n\tchain:\t{}", cons, chain);
        ConstructorSup<Object> nextCon = null;
        try {
            nextCon = chain.next().handle(scope, cons, chain);
        } catch (AnnoHandleException ignored) {
        }

        if (nextCon != null) {
            return nextCon;
        }

        LOGGER.debug("start handling with DefaultConstructorFilter");

        if (cons.size() == 1) {
            return cons.get(0);
        }
        if (cons.isEmpty()) {
            return null;
        }
        return If($(cons).findOne(c -> c.argCount() == 0), c -> c).Else(() -> {
            throw new AnnoHandleException("Constructor choices are ambiguous");
        });

    }

}
