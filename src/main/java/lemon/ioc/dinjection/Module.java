package lemon.ioc.dinjection;

import lemon.ioc.dinjection.binder.Binder;

public interface Module {

    void configure(Binder binder);
}
