package net.cassite.ioc.testioc;

import lemon.needle.aop.AOP;

@AOP(value = ThrowWeaver.class, useCglib = true)
public class ThrowAOPBean {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        throw new RuntimeException();
    }
}
