package net.cassite.ioc.testioc;

import net.cassite.pure.aop.AOP;

@AOP(value = TargetAwareWeaver.class)
public class TargetAwareAOPBean {
    public void doSomething() {
    }
}
