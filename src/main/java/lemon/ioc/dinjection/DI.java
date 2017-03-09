package lemon.ioc.dinjection;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.ioc.dinjection.binder.Scope;

public class DI {

    private static final Logger logger = LoggerFactory.getLogger(DI.class);

    private DI() {
        logger.info("");
    }

    public static Injector createInjector(Module... modules) {
        return createInjector(null, Arrays.asList(modules));
    }

    public static Injector createInjector(Scope scope, Iterable<? extends Module> modules) {
        synchronized (DI.class) {
            Injector inject = new Injector();
            inject.autoRegister();

            return inject;
        }
    }

}
