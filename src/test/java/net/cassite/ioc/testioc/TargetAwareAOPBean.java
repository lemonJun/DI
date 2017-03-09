package net.cassite.ioc.testioc;

import lemon.ioc.aop.AOP;

@AOP(value = TargetAwareWeaver.class)
public class TargetAwareAOPBean {
    public void doSomething() {
    }
}
