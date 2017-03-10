package lemon.needle;

import lemon.needle.ioc.Needle;
import lemon.needle.ioc.Injector;

public class NeedleHolder {

    public static Injector injector;

    public static void init() {
        injector = Needle.createInjector(new BindingModel());
    }

    public static <T> T getInstance(Class<T> type) {
        return injector.getInstance(type);
    }
}
