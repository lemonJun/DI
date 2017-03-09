package lemon.ioc.di;

import lemon.ioc.di.binder.Binder;

public interface Module {

    void configure(Binder binder);
}
