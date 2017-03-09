package lemon.ioc;

import lemon.ioc.di.DI;
import lemon.ioc.di.Injector;

public class DIHolder {

    public static Injector injector;

    public static void init() {
        injector = DI.createInjector(new BindingModel());
    }

    public static <T> T getInstance(Class<T> type) {
        return injector.getInstance(type);
    }
}
