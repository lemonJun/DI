package lemon.ioc.aop;

import static net.cassite.style.Style.$;
import static net.cassite.style.Style.sleep;
import static net.cassite.style.aggregation.Aggregation.$;
import static net.cassite.style.util.Utils.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.ioc.di.DI;
import lemon.ioc.di.Injector;
import lemon.ioc.di.Scope;
import net.cassite.style.interfaces.RFunc0;

/**
 * Controls AOP processes<br>
 * use @AOP and weavers to enhance your class<br>
 * e.g.<br>
 * <code>
 * \@AOP(YourHandlerClass.class)<br>
 * class YourClassToBeEnhanced{...}
 * </code>
 *
 * @author lemon
 * @since 0.1.1
 */
public class AOPController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AOPController.class);

    private final Injector injector;

    public AOPController(Injector injector) {
        this.injector = injector;
    }

    /**
     * retrieve proxy object
     *
     * @param scope          ioc scope
     * @param objFunc        enable aop on the function returned object
     * @param expectingClass class expected to retrieve
     * @param <T>            expecting type
     * @param <U>            object type
     * @return proxy object
     */
    @SuppressWarnings("unchecked")
    public static <T, U extends T> T weave(Scope scope, RFunc0<U> objFunc, Class<T> expectingClass) {
        if (scope == null) {
            scope = new Scope(Scope.currentThreadScope());
        }
        final Scope s = scope;
        LOGGER.debug("Weaving : object function {}, expectingClass {}", objFunc, expectingClass);
        AOP aop = expectingClass.getAnnotation(AOP.class);

        if (aop == null) {
            try {
                return objFunc.apply();
            } catch (Throwable throwable) {
                throw $(throwable);
            }
        } else {
            Weaver[] weavers;
            weavers = new Weaver[aop.value().length];
            $(aop.value()).forEach((e, i) -> {
                Weaver w = (Weaver) DI.createInjector(null).getInstance(s, e);
                if (w instanceof TargetAware) {
                    ((TargetAware<U>) w).targetAware(objFunc);
                }
                weavers[$(i)] = w;
            });
            LOGGER.debug("retrieved weavers are {}", (Object) weavers);
            Generator<U> generator = new Generator<>(objFunc);
            boolean useCglib = aop.useCglib();

            Handler h;
            if (useCglib || expectingClass.getInterfaces().length == 0) {
                h = new CglibHandler(weavers, generator, expectingClass);
            } else {
                h = new JDKHandler(weavers, generator, expectingClass);
            }

            // set delay and destroy
            long timeout = aop.timeoutMillis();
            if (timeout > 0) {
                run(() -> {
                    sleep(timeout);
                    h.destroy();
                });
            }

            return (T) h.proxy();
        }
    }

    /**
     * retrieve proxy object
     *
     * @param objFunc        enable aop on the function returned object
     * @param expectingClass class expected to retrieve
     * @param <T>            expecting type
     * @param <U>            object type
     * @return proxy object
     */
    public static <T, U extends T> T weave(RFunc0<U> objFunc, Class<T> expectingClass) {
        return weave(null, objFunc, expectingClass);
    }
}
