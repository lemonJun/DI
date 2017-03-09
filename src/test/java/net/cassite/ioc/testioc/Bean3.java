package net.cassite.ioc.testioc;

import net.cassite.pure.ioc.annotations.ScopeAttr;
import net.cassite.pure.ioc.annotations.Wire;

@Wire
public class Bean3 {
    private BeanA beanA;

    public BeanA getBeanA() {
        return beanA;
    }

    public void setBeanA(@ScopeAttr("beanA") BeanA beanA) {
        this.beanA = beanA;
    }
}
