package net.cassite.ioc.testioc;

import lemon.ioc.di.annotations.Force;
import lemon.ioc.di.annotations.Inject;

public class TestWireLogicBean {
    String string;
    int integer;

    public void setInteger(int integer) {
        this.integer = integer;
    }
}

class TestWireLogicBeanWithWireOnClass {
    @Force("a")
    String string;
    int integer;

    @Force("1")
    public void setInteger(int integer) {
        this.integer = integer;
    }
}

class TestWireLogicBeanWithWireOnField {
    @Inject
    @Force("a")
    String string;
    int integer;

    public void setInteger(int integer) {
        this.integer = integer;
    }
}

class TestWireLogicBeanWithWireOnSetter {
    String string;
    int integer;

    @Inject
    @Force("1")
    public void setInteger(int integer) {
        this.integer = integer;
    }
}