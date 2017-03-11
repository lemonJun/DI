package lemon.needle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.needle.ioc.Injector;
import lemon.needle.ioc.Module;

public class Needle {

    private static final Logger logger = LoggerFactory.getLogger(Needle.class);

    private Needle() {
        logger.info("");
    }

    public static Injector injector;

    public static void init() {
        createInjector(new BindingModel());
    }

    public static void createInjector(Module... modules) {
        injector = Injector.with(modules);
    }

    public static <T> T getInstance(Class<T> type) {
        return injector.instance(type);
    }

}
