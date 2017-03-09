package lemon.ioc;

import lemon.ioc.dinjection.DI;
import lemon.ioc.dinjection.Injector;

public class DIHolder {

    public static Injector injector;

    public static void init() {
        injector = DI.createInjector(new BindingModel());
    }

    public static <T> T getInstance(Class<T> type) {
        return injector.getInstance(type);
    }
}
