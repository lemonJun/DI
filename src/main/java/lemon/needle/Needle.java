package lemon.needle;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.needle.ioc.Injector;
import lemon.needle.ioc.Module;

public class Needle {

    private static final Logger logger = LoggerFactory.getLogger(Needle.class);

    private AtomicBoolean initOnce = new AtomicBoolean(false);

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
