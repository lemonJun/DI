package net.cassite.ioc.testioc;

import lemon.ioc.di.annotations.Inject;
import lemon.ioc.di.annotations.ScopeAttr;

public class Bean1 {
    private BeanA beanA;
    private Bean2 bean2;
    private Bean3 bean3;

    public BeanA getBeanA() {
        return beanA;
    }

    public void setBeanA(@ScopeAttr("beanA") BeanA beanA) {
        this.beanA = beanA;
    }

    public Bean2 getBean2() {
        return bean2;
    }

    public void setBean2(Bean2 bean2) {
        this.bean2 = bean2;
    }

    public Bean3 getBean3() {
        return bean3;
    }

    public void setBean3(Bean3 bean3) {
        this.bean3 = bean3;
    }
}
