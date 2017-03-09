package net.cassite.ioc.testioc;

import net.cassite.pure.aop.AOP;

@AOP(value = { ChangeArgBeforeInvokingWeaver.class, ChangeArgAfterInvokingWeaver.class, ThrowWeaver.class }, useCglib = true)
public class AllAOPBean {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String name() {
        throw new RuntimeException();
    }
}
