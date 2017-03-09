package net.cassite.ioc.testioc;

import lemon.ioc.dinjection.annotations.Inject;
import lemon.ioc.dinjection.annotations.ScopeAttr;

public class Bean2 {
    private BeanA beanA;

    public BeanA getBeanA() {
        return beanA;
    }

    public void setBeanA(@ScopeAttr("beanA") BeanA beanA) {
        this.beanA = beanA;
    }
}
