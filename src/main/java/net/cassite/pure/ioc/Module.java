package net.cassite.pure.ioc;

import net.cassite.pure.ioc.binder.Binder;

public interface Module {

    void configure(Binder binder);
}
