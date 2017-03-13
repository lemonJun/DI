package lemon.needle.ioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.needle.NeedleModel;

public class Needle {

    private static final Logger logger = LoggerFactory.getLogger(Needle.class);

    private Needle() {
        logger.info("");
    }

    public static InjectorImpl injector;

    public static void init() {
        createInjector(new NeedleModel());
    }

    public static void createInjector(Module... modules) {
        injector = InjectorImpl.with(modules);
    }

    public static <T> T getInstance(Class<T> type) {
        return injector.instance(type);
    }

}
