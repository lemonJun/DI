package lemon.needle.ioc;

import lemon.needle.ioc.binder.Binder;

public interface Module {

    void configure(Binder binder);
}
