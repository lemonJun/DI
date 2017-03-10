package net.cassite.ioc.testioc;

import lemon.needle.ioc.annotations.Inject;
import lemon.needle.ioc.annotations.ScopeAttr;

public class Bean2 {
    private BeanA beanA;

    public BeanA getBeanA() {
        return beanA;
    }

    public void setBeanA(@ScopeAttr("beanA") BeanA beanA) {
        this.beanA = beanA;
    }
}
