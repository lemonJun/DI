package net.cassite.ioc.testioc;

import lemon.needle.aop.AOP;

@AOP(value = ChangeArgAfterInvokingWeaver.class, useCglib = true)
public class ChangeReturnAfterInvokingAOPBean {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
