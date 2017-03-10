package lemon.needle.ioc;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.needle.ioc.binder.Scope;

public class Needle {

    private static final Logger logger = LoggerFactory.getLogger(Needle.class);

    private Needle() {
        logger.info("");
    }

    public static Injector createInjector(Module... modules) {
        return createInjector(null, Arrays.asList(modules));
    }

    public static Injector createInjector(Scope scope, Iterable<? extends Module> modules) {
        synchronized (Needle.class) {
            Injector inject = new Injector();
            inject.autoRegister();

            return inject;
        }
    }

}
