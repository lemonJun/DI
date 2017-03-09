package net.cassite.pure;

import net.cassite.pure.ioc.DI;
import net.cassite.pure.ioc.Injector;

public class DIHolder {

    public static Injector injector;

    public static void init() {
        injector = DI.createInjector(new BindingModel());
    }

    public static <T> T getInstance(Class<T> type) {
        return injector.getInstance(type);
    }
}
